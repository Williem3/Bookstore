package com.bookstore.service.serviceimpl;

import com.bookstore.dao.BookDAO;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDAO bookDAO;

    @Override
    public List<Book> findAll() {
        return (List<Book>) bookDAO.findAll();
    }

    @Override
    public Book findOne(Long id) {
        return bookDAO.findById(id).orElse(null);
    }
}
