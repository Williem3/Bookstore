package com.bookstore.dao;

import com.bookstore.entity.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartDAO extends CrudRepository<ShoppingCart, Long> {
}
