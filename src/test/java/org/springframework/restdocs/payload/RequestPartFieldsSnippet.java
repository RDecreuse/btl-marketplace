package org.springframework.restdocs.payload;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.snippet.Snippet;

import java.io.IOException;
import java.util.List;

/**
 * A {@link Snippet} that documents the fields of a specific request part.
 */
public class RequestPartFieldsSnippet extends AbstractFieldsSnippet {

    private String partName;

    public RequestPartFieldsSnippet(final String partName, final List<org.springframework.restdocs.payload.FieldDescriptor> descriptors) {
        super("request", descriptors, null, false);
        this.partName = partName;
    }

    @Override
    protected MediaType getContentType(final Operation operation) {
        for (final OperationRequestPart candidate : operation.getRequest().getParts()) {
            if (candidate.getName().equals(this.partName)) {
                return candidate.getHeaders().getContentType();
            }
        }
        return null;
    }

    @Override
    protected byte[] getContent(final Operation operation) throws IOException {
        for (final OperationRequestPart candidate : operation.getRequest().getParts()) {
            if (candidate.getName().equals(this.partName)) {
                return candidate.getContent();
            }
        }
        return new byte[0];
    }
}
