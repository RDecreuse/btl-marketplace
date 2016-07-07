package com.btl.doc.service;

import com.btl.doc.business.Product;
import com.btl.doc.repository.ProductDao;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao dao;

    @Override
    public List<Product> getProducts() {
        return Lists.newArrayList(dao.findAll());
    }

    @Override
    public Product findOne(Long productId) {
        return dao.findOne(productId);
    }

    @Override
    public Product create(Product product) {
        return dao.save(product);
    }

    @Override
    public Product update(Product product) {
        if(StringUtils.isEmpty(product.getId())){
            throw new IllegalArgumentException("Product must have an id.");
        }
        return dao.save(product);
    }

    @Override
    public void delete(Long productId) {
        this.dao.delete(productId);
    }
}
