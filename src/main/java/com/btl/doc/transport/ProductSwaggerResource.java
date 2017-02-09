package com.btl.doc.transport;

import com.btl.doc.business.Product;
import com.btl.doc.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController
@Api(value = "Resource for products", description = "Resource for managing products")
@RequestMapping("/api/products")
public class ProductSwaggerResource {

    @Autowired
    private ProductService service;

    @GET
    @Path("/")
    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return a list of product", notes = "The list may be empty.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return results"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public List<Product> findAll() {
        return this.service.getProducts();
    }

    @GET
    @Path("/{productId}")
    @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return a product for given id", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return results"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 404, message = "No product found for given id."),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Product findOne(@ApiParam(value = "Product id", required = true) @PathVariable final Long productId) {
        return this.service.findOne(productId);
    }

    @POST
    @Path("/")
    @RequestMapping(method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a product and return it.", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return new product"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Product create(@ApiParam(value = "Fresh product", required = true) @RequestBody Product product) {
        return service.create(product);
    }

    @PUT
    @Path("/{productId}")
    @RequestMapping(path = "/{productId}", method = RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a product and return it.", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return updated product"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Product update(@ApiParam(value = "Updated product", required = true) @RequestBody Product product) {
        return service.update(product);
    }

    @DELETE
    @Path("/{productId}")
    @RequestMapping(path = "/{productId}", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a product.", notes = "No result will be sent.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, product deleted."),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public void delete(@PathVariable Long productId) {
        service.delete(productId);
    }

}
