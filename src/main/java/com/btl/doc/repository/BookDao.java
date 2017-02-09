package com.btl.doc.repository;

import com.btl.doc.business.Book;
import com.btl.doc.business.Offer;
import org.springframework.data.repository.CrudRepository;

public interface BookDao extends CrudRepository<Book, Long> {

}
