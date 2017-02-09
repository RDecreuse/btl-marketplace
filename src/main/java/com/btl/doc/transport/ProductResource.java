package com.btl.doc.transport;

import com.btl.doc.business.Product;
import com.btl.doc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products2")
public class ProductResource {

    @Autowired
    private ProductService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> findAll() {
        return this.service.getProducts();
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Product> findOne(@PathVariable final Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.service.findOne(productId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(product));
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Product> update(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(product));
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long productId) {
        service.delete(productId);
    }

}
