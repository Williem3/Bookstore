package com.bookstore.service;

import com.bookstore.entity.Payment;
import com.bookstore.entity.UserPayment;

public interface PaymentService {
    Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
