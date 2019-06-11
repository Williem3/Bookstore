package com.bookstore.dao;

import com.bookstore.entity.UserShipping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserShippingDAO extends CrudRepository<UserShipping, Long> {
}
