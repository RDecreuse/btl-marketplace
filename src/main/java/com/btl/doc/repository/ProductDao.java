package com.btl.doc.repository;

import com.btl.doc.business.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Long> {

}
