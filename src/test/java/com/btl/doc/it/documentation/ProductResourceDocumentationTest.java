package com.btl.doc.it.documentation;

import com.btl.doc.it.DocumentationTest;
import com.btl.doc.transport.ProductResource;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductResourceDocumentationTest extends DocumentationTest<ProductResource> {

    @Test
    public void testFindAll() throws Exception {

        this.mockMvc.perform(get(getUrlForResource() + "/"))
                .andExpect(status().isOk())
                .andDo(
                        document("product-find",
                                getDocRequestPreprocess(),
                                responseFields(Lists.newArrayList(
                                        fieldWithPath("[].id").description("Product id"),
                                        fieldWithPath("[].name").description("Product name"),
                                        fieldWithPath("[].comment").description("Product comment"),
                                        fieldWithPath("[].offers").description("Product offers"),
                                        fieldWithPath("[].offers[].id").description("offer id"),
                                        fieldWithPath("[].offers[].productId").description("Offer productId"),
                                        fieldWithPath("[].offers[].price").description("Offer price"),
                                        fieldWithPath("[].offers[].comment").description("Offer comment"),
                                        fieldWithPath("[].offers[].name").description("Offer name")
                                        )
                                )
                        )
                );
    }

    @Test
    public void testFindOne() throws Exception {

        this.mockMvc.perform(get(getUrlForResource() + "/{productId}", "1")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("product-find-one",
                                getDocRequestPreprocess(),
                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("productId").description("Product Id")),
                                responseFields(Lists.newArrayList(
                                        fieldWithPath("id").description("Product id"),
                                        fieldWithPath("name").description("Product name"),
                                        fieldWithPath("comment").description("Product comment"),
                                        fieldWithPath("offers").description("Product offers"),
                                        fieldWithPath("offers[].id").description("offer id"),
                                        fieldWithPath("offers[].productId").description("Offer productId"),
                                        fieldWithPath("offers[].price").description("Offer price"),
                                        fieldWithPath("offers[].comment").description("Offer comment"),
                                        fieldWithPath("offers[].name").description("Offer name")
                                        )
                                )
                        )
                );
    }

    @Test
    public void testCreateUpdateDelete() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post(getUrlForResource())
                .content("{\"name\":\"testName\",\"comment\":\"product comment test\"}")
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andDo(
                        document("product-create",
                                getDocRequestPreprocess(),
                                requestFields(
                                        fieldWithPath("name").description("Product name").optional(),
                                        fieldWithPath("comment").description("Product comment").optional()
                                ),
                                responseFields(Lists.newArrayList(
                                        fieldWithPath("id").description("Product id"),
                                        fieldWithPath("name").description("Product name"),
                                        fieldWithPath("comment").description("Product comment")
                                        ))
                        )
                )
                .andReturn();

        String freshlyCreatedProductId = getFirstIdAsStringFromBodyOrLocation(mvcResult);

        this.mockMvc.perform(put(getUrlForResource() + "/{productId}", freshlyCreatedProductId)
                .content("{\"id\":\""+freshlyCreatedProductId+"\",\"name\":\"testName2\",\"comment\":\"product comment test2\"}")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(
                        document("product-update",
                                getDocRequestPreprocess(),
                                requestFields(
                                        fieldWithPath("id").description("Product id").optional(),
                                        fieldWithPath("name").description("Product name").optional(),
                                        fieldWithPath("comment").description("Product comment").optional()
                                ),
                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("productId").description("Product Id")),
                                responseFields(Lists.newArrayList(
                                        fieldWithPath("id").description("Product id"),
                                        fieldWithPath("name").description("Product name"),
                                        fieldWithPath("comment").description("Product comment")
                                ))
                        )
                );

        this.mockMvc.perform(delete(getUrlForResource() + "/{productId}", freshlyCreatedProductId))
                .andExpect(status().isOk())
                .andDo(
                        document("product-delete",
                                getDocRequestPreprocess(),
                                PathParamSnippet.pathParamSnippet(RequestDocumentation.parameterWithName("productId").description("Product Id"))
                        )
                );
    }
}
