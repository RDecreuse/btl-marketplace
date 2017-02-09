package com.btl.doc.service;

import com.btl.doc.business.Book;
import com.btl.doc.business.Product;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    Book findOne(Long bookId);

    Book create(Book book);

    Book update(Book book);

    void delete(Long bookId);
}
