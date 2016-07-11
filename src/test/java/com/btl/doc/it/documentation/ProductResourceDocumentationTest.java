package com.btl.doc.it.documentation;

import com.btl.doc.it.DocumentationTest;
import com.btl.doc.transport.ProductResource;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.PathParamSnippet;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MvcResult;

import static com.btl.doc.it.DocumentationFields.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductResourceDocumentationTest extends DocumentationTest<ProductResource> {

    @Test
    public void testFindWithFilters() throws Exception {
//        String bearer = getJohnDoeBearer();

        this.mockMvc.perform(post(getUrlForResource() + "/find")
                .content("{\"startDate\":\"2015-01-01\",\"endDate\":\"2099-01-01\",\"observationType\":\"OTHER\",\"cropId\":\"7\",\"userId\":\"3\",\"farmId\":\"3\", \"geometry\":\"{\\\"type\\\": \\\"Polygon\\\", \\\"coordinates\\\": [[ [41, 41], [41, 43], [43, 43], [43, 41], [41, 41]]]}\"}")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(
                        document("observation-find",
                                getDocRequestPreprocess(),
                                requestFields(
                                        fieldWithPath("geometry").description("Geometry which contains observation field").optional(),
                                        fieldWithPath("startDate").description("Start date to filter over a period").optional(),
                                        fieldWithPath("endDate").description("End date to filter over a period").optional(),
                                        fieldWithPath("observationType").description("Observation type filtering").optional(),
                                        fieldWithPath("cropId").description("Crop id for crop filter").optional(),
                                        fieldWithPath("userId").description("User id for user filter").optional(),
                                        fieldWithPath("farmId").description("Farm id for farm filter").optional()
                                ),
                                toResponseField(addFieldsInResources(getObservationFieldDescription(true), "_embedded.observationRepresentationList[]"))
                        )
                );
    }

    @Test
    public void testFindOne() throws Exception {

        this.mockMvc.perform(get(getUrlForResource() + "/{observationId}", "1")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("observation-find-one",
                                getDocRequestPreprocess(),
                                toResponseField(getObservationFieldDescription(true)),
                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("observationId").description("Observation Id")),
                                links(
                                        linkWithRel("search").description("The link to search observations : <<observation-find>>"),
                                        linkWithRel("types").description("The link to find observations types : <<observation-types>>"),
                                        linkWithRel("self").description("The link to current observation : <<observation-get>>")
                                )
                        )
                );
    }

    @Test
    public void testCreateUpdateDelete() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post(getUrlForResource())
                .content("{\"cropId\":\"7\",\"userId\":\"3\",\"observationType\":\"OTHER\",\"observationDate\":\"2016-01-01\",\"creationDate\":\"\",\"attributes\":{\"comment\":\"nomNom\"}}")
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andDo(
                        document("observation-create",
                                getDocRequestPreprocess(),
                                requestFields(
                                        fieldWithPath("cropId").description("Observation crop id").optional(),
                                        fieldWithPath("userId").description("Observation user id").optional(),
                                        fieldWithPath("observationType").description("Observation type").optional(),
                                        fieldWithPath("observationDate").description("Observation date").optional(),
                                        fieldWithPath("creationDate").description("Observation creation date (generated if not specified)").optional(),
                                        fieldWithPath("attributes").description("Observation attributes").optional()
                                ),
                                toResponseField(getObservationFieldDescription(true)),
                                links(
                                        linkWithRel("search").description("The link to search observations : <<observation-find>>"),
                                        linkWithRel("types").description("The link to find observations types : <<observation-types>>"),
                                        linkWithRel("self").description("The link to current observation : <<observation-get>>")
                                )
                        )
                )
                .andReturn();

        String freshlyCreatedObservationId = getFirstIdAsStringFromBodyOrLocation(mvcResult);

        this.mockMvc.perform(put(getUrlForResource() + "/{observationId}", freshlyCreatedObservationId)
                .content("{\"cropId\":\"7\",\"userId\":\"3\",\"observationType\":\"PESTS\",\"observationDate\":\"2016-01-01\",\"creationDate\":\"2016-02-02\",\"attributes\":{}}")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(
                        document("observation-update",
                                getDocRequestPreprocess(),
                                toResponseField(getObservationFieldDescription(true)),
                                requestFields(
                                        fieldWithPath("cropId").description("Observation crop id").optional(),
                                        fieldWithPath("userId").description("Observation user id").optional(),
                                        fieldWithPath("observationType").description("Observation type").optional(),
                                        fieldWithPath("observationDate").description("Observation date").optional(),
                                        fieldWithPath("creationDate").description("Observation creation date (generated if not specified)").optional(),
                                        fieldWithPath("attributes").description("Observation attributes").optional()
                                ),
                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("observationId").description("Observation Id")),
                                links(
                                        linkWithRel("search").description("The link to search observations : <<observation-find>>"),
                                        linkWithRel("types").description("The link to find observations types : <<observation-types>>"),
                                        linkWithRel("self").description("The link to current observation : <<observation-get>>")
                                )
                        )
                );

//        this.mockMvc.perform(delete(getUrlForResource() + "/{observationId}", freshlyCreatedObservationId)
//                .header(AUTHORIZATION_HEADER, bearer))
//                .andExpect(status().isOk())
//                .andDo(
//                        document("observation-delete",
//                                getDocRequestPreprocess(),
//                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("observationId").description("Observation Id"))
//                        )
//                );
    }

    @Test
    public void testFindTypes() throws Exception {
//        this.mockMvc.perform(get(getUrlForResource() + "/types")
//                .header(AUTHORIZATION_HEADER, bearer)
//                .accept(MediaTypes.HAL_JSON))
//                .andExpect(status().isOk())
//                .andDo(
//                        document("observation-types",
//                                getDocRequestPreprocess(),
//                                getDocResponsePreprocess(),
//                                toResponseField(addFieldsInResources(getObservationAndInterventionTypeFieldDescription(), "_embedded.observationTypeList[]"))
//                        )
//                );
    }
}
