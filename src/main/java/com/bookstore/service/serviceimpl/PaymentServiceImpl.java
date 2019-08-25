package com.bookstore.service.serviceimpl;

import com.bookstore.entity.Payment;
import com.bookstore.entity.UserPayment;
import com.bookstore.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
        payment.setType(userPayment.getType());
        payment.setCardName(userPayment.getCardName());
        payment.setCardNumber(userPayment.getCardNumber());
        payment.setCvc(userPayment.getCvc());
        payment.setExpiryMonth(userPayment.getExpiryMonth());
        payment.setExpiryYear(userPayment.getExpiryYear());
        payment.setHolderName(userPayment.getHolderName());

        return payment;
    }
}
