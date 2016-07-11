package com.btl.doc.transport;

import com.btl.doc.business.Product;
import com.btl.doc.service.ProductService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> findAll() {
        return this.service.getProducts();
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
    public Product findOne(@PathVariable final Long productId) {
        return this.service.findOne(productId);
    }

    @RequestMapping(method = RequestMethod.POST)
    Product create(@RequestBody Product product) {
        return service.create(product);
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.PUT)
    Product update(@RequestBody Product product) {
        return service.update(product);
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.DELETE)
    void delete(@PathVariable Long productId) {
        service.delete(productId);
    }

}
