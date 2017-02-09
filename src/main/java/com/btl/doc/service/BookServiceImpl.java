package com.btl.doc.service;

import com.btl.doc.business.Book;
import com.btl.doc.business.Product;
import com.btl.doc.repository.BookDao;
import com.btl.doc.repository.ProductDao;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookDao dao;

    @Override
    public List<Book> getBooks() {
        return Lists.newArrayList(dao.findAll());
    }

    @Override
    public Book findOne(Long bookId) {
        return dao.findOne(bookId);
    }

    @Override
    public Book create(Book book) {
        return dao.save(book);
    }

    @Override
    public Book update(Book book) {
        if(StringUtils.isEmpty(book.getId())){
            throw new IllegalArgumentException("Book must have an id.");
        }
        return dao.save(book);
    }

    @Override
    public void delete(Long bookId) {
        this.dao.delete(bookId);
    }
}
