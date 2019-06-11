package com.bookstore.dao;

import com.bookstore.entity.UserPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentDAO extends CrudRepository<UserPayment, Long> {
}
