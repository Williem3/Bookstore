package com.bookstore.service;

import com.bookstore.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    Book findOne(Long id);
}
