package com.bookstore.dao;

import com.bookstore.entity.Book;
import org.springframework.data.repository.CrudRepository;


public interface BookDAO extends CrudRepository<Book, Long> {
}
