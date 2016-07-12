package com.btl.doc.it;

import com.btl.doc.DocBtlApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DocBtlApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebIntegrationTest({"server.port=0", "management.port=0"})
@Slf4j
public abstract class DocumentationTest<T> {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(log())
                .build();
    }

    protected static ResultHandler log() {
        return result -> {
            LOGGER.debug(result.getResponse().getStatus() + "  - " + result.getResponse().getErrorMessage());
            LOGGER.debug(result.getResponse().getContentAsString());
        };
    }

    protected static OperationRequestPreprocessor getDocRequestPreprocess(final OperationPreprocessor... preprocessors) {
        return preprocessRequest(
                prettyPrint(),
                removeHeaders("Host"), new OperationPreprocessor() {
                    @Override
                    public OperationRequest preprocess(final OperationRequest request) {
                        if (preprocessors != null) {
                            OperationRequest r = request;
                            for (final OperationPreprocessor preprocessor : preprocessors) {
                                r = preprocessor.preprocess(r);
                            }
                            return r;
                        } else {
                            return request;
                        }
                    }

                    @Override
                    public OperationResponse preprocess(final OperationResponse response) {
                        if (preprocessors != null) {
                            OperationResponse r = response;
                            for (final OperationPreprocessor preprocessor : preprocessors) {
                                r = preprocessor.preprocess(r);
                            }
                            return r;
                        } else {
                            return response;
                        }
                    }
                }
        );
    }


    protected String getUrlForResource() {
        return getUrlForResource(getParametrizedType());
    }

    private String getUrlForResource(final Class resourceClass) {
        return url() + ((RequestMapping) resourceClass.getAnnotation(RequestMapping.class)).value()[0];
    }

    private Class getParametrizedType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String url() {
        return "http://localhost";
    }

    protected String getFirstIdAsStringFromBodyOrLocation(final MvcResult result) throws UnsupportedEncodingException {
        String freshlyCreatedResourceId = null;

        MockHttpServletResponse response = result.getResponse();
        String locationHeader = response.getHeader("Location");
        if (locationHeader != null) {
            String[] split = locationHeader.split("/");
            freshlyCreatedResourceId = split[split.length - 1];
        } else {
            String responseString = response.getContentAsString();
            Matcher matcher = Pattern.compile("id\":\\d+").matcher(responseString);
            if (matcher.find()) {
                freshlyCreatedResourceId = matcher.group().substring(4);
            } else {
                Assert.fail("Resource not created, can't retrieve its id");
            }
        }

        return freshlyCreatedResourceId;
    }
}
