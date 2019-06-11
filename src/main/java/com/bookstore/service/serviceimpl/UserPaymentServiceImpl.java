package com.bookstore.service.serviceimpl;

import com.bookstore.dao.UserPaymentDAO;
import com.bookstore.entity.UserPayment;
import com.bookstore.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private UserPaymentDAO userPaymentDAO;


    @Override
    public UserPayment findById(Long id) {
        return userPaymentDAO.findById(id).orElse(null);
    }

    @Override
    public void removeById(Long creditCardId) {
        userPaymentDAO.deleteById(creditCardId);
    }
}
