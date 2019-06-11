package com.bookstore.service;

import com.bookstore.entity.UserPayment;

public interface UserPaymentService {
    UserPayment findById(Long id);

    void removeById(Long creditCardId);
}
