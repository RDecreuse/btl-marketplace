package org.springframework.restdocs;

import com.google.common.collect.Lists;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.request.AbstractParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.SnippetException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParamSnippet extends AbstractParametersSnippet {

    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private static final String MULTIPART_BOUNDARY = "6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm";

    public PathParamSnippet(final ParameterDescriptor... descriptors) {
        super("path-param-custom", Lists.newArrayList(descriptors), null, false);
    }

    public static PathParamSnippet pathParamSnippet(final ParameterDescriptor... descriptors) {
        return new PathParamSnippet(descriptors);
    }

    private static String getParameterName(final String match) {
        int colonIndex = match.indexOf(':');
        return colonIndex != -1 ? match.substring(0, colonIndex) : match;
    }

    @Override
    protected Map<String, Object> createModel(final Operation operation) {
        Map<String, Object> model = super.createModel(operation);
        model.put("path", removeQueryStringIfPresent(extractUrlTemplate(operation)));
        model.put("method", operation.getRequest().getMethod());
        model.put("headers", getHeaders(operation.getRequest()));
        model.put("requestBody", getRequestBody(operation.getRequest()));
        return model;
    }

    private String removeQueryStringIfPresent(final String urlTemplate) {
        int index = urlTemplate.indexOf('?');
        if (index == -1) {
            return urlTemplate;
        }
        return urlTemplate.substring(0, index);
    }

    private String extractUrlTemplate(final Operation operation) {
        String urlTemplate = (String) operation.getAttributes().get("org.springframework.restdocs.urlTemplate");
        Assert.notNull(urlTemplate,
                "urlTemplate not found. Did you use RestDocumentationRequestBuilders to "
                        + "build the request?");
        // try to extract host and port from url
        Matcher matcher = Pattern.compile("/v[1-9].+").matcher(urlTemplate);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return urlTemplate;
        }
    }

    private List<Map<String, String>> getHeaders(final OperationRequest request) {
        List<Map<String, String>> headers = new ArrayList<>();

        for (final Map.Entry<String, List<String>> header : request.getHeaders().entrySet()) {
            for (final String value : header.getValue()) {
                if (Objects.equals(header.getKey(), HttpHeaders.CONTENT_TYPE)
                        && !request.getParts().isEmpty()) {
                    headers.add(header(header.getKey(),
                            String.format("%s; boundary=%s", value, MULTIPART_BOUNDARY)));
                } else {
                    headers.add(header(header.getKey(), value));
                }

            }
        }
        if (requiresFormEncodingContentTypeHeader(request)) {
            headers.add(header(HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }
        return headers;
    }

    private String getRequestBody(final OperationRequest request) {
        StringWriter httpRequest = new StringWriter();
        PrintWriter writer = new PrintWriter(httpRequest);
        String content = request.getContentAsString();
        if (StringUtils.hasText(content)) {
            writer.printf("%n%s", content);
        } else if (isPutOrPost(request)) {
            if (request.getParts().isEmpty()) {
                String queryString = request.getParameters().toQueryString();
                if (StringUtils.hasText(queryString)) {
                    writer.println();
                    writer.print(queryString);
                }
            } else {
                writeParts(request, writer);
            }
        }
        return httpRequest.toString();
    }

    private Map<String, String> header(final String name, final String value) {
        Map<String, String> header = new HashMap<>();
        header.put("name", name);
        header.put("value", value);
        return header;
    }

    private boolean requiresFormEncodingContentTypeHeader(final OperationRequest request) {
        return request.getHeaders().get(HttpHeaders.CONTENT_TYPE) == null
                && isPutOrPost(request) && !request.getParameters().isEmpty();
    }

    private boolean isPutOrPost(final OperationRequest request) {
        return HttpMethod.PUT.equals(request.getMethod())
                || HttpMethod.POST.equals(request.getMethod());
    }

    private void writeParts(final OperationRequest request, final PrintWriter writer) {
        writer.println();
        for (final Map.Entry<String, List<String>> parameter : request.getParameters().entrySet()) {
            for (final String value : parameter.getValue()) {
                writePartBoundary(writer);
                writePart(parameter.getKey(), value, null, writer);
                writer.println();
            }
        }
        for (final OperationRequestPart part : request.getParts()) {
            writePartBoundary(writer);
            writePart(part, writer);
            writer.println();
        }
        writeMultipartEnd(writer);
    }

    private void writePartBoundary(final PrintWriter writer) {
        writer.printf("--%s%n", MULTIPART_BOUNDARY);
    }

    private void writePart(final String name, final String value, final MediaType contentType,
                           final PrintWriter writer) {
        writer.printf("Content-Disposition: form-data; name=%s%n", name);
        if (contentType != null) {
            writer.printf("Content-Type: %s%n", contentType);
        }
        writer.println();
        writer.print(value);
    }

    private void writePart(final OperationRequestPart part, final PrintWriter writer) {
        writePart(part.getName(), part.getContentAsString(), part.getHeaders()
                .getContentType(), writer);
    }

    private void writeMultipartEnd(final PrintWriter writer) {
        writer.printf("--%s--", MULTIPART_BOUNDARY);
    }

    @Override
    protected Set<String> extractActualParameters(final Operation operation) {
        String urlTemplate = extractUrlTemplate(operation);
        Matcher matcher = NAMES_PATTERN.matcher(urlTemplate);
        Set<String> actualParameters = new HashSet<>();
        while (matcher.find()) {
            String match = matcher.group(1);
            actualParameters.add(getParameterName(match));
        }
        return actualParameters;
    }

    @Override
    protected void verificationFailed(final Set<String> undocumentedParameters,
                                      final Set<String> missingParameters) {
        String message = "";
        if (!undocumentedParameters.isEmpty()) {
            message += "Path parameters with the following names were not documented: "
                    + undocumentedParameters;
        }
        if (!missingParameters.isEmpty()) {
            if (message.length() > 0) {
                message += ". ";
            }
            message += "Path parameters with the following names were not found in "
                    + "the request: " + missingParameters;
        }
        throw new SnippetException(message);
    }
}
