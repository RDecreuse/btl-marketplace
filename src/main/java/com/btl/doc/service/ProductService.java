package com.btl.doc.service;

import com.btl.doc.business.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product findOne(Long productId);

    Product create(Product product);

    Product update(Product product);

    void delete(Long productId);

}
