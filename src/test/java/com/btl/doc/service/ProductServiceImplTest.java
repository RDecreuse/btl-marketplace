package com.btl.doc.service;

import com.btl.doc.business.Product;
import com.btl.doc.repository.ProductDao;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl service;

    @Mock
    ProductDao dao;

    @Test
    public void testGetProducts() {
        Mockito.when(dao.findAll()).thenReturn(Lists.newArrayList());
        this.service.getProducts();
    }

    @Test
    public void testFindOne() {
        this.service.findOne(1L);
    }

    @Test
    public void testCreate() {
        this.service.create(new Product());
    }

    @Test
    public void testUpdate() {
        Product product = new Product();
        product.setId(1L);
        this.service.update(product);
    }

    @Test
    public void testDelete() {
        this.service.delete(1L);
    }


}