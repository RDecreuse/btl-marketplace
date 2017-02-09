package com.btl.doc.repository;

import com.btl.doc.business.Author;
import com.btl.doc.business.Book;
import org.springframework.data.repository.CrudRepository;

public interface AuthorDao extends CrudRepository<Author, Long> {

}
