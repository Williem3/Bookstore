package com.bookstore.service;

import com.bookstore.entity.UserShipping;

public interface UserShippingService {
    UserShipping findById(Long shippingAddressId);

    void removeById(Long id);
}
