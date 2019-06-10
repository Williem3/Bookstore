package com.bookstore.dao;

import com.bookstore.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
