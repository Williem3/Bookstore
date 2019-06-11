package com.bookstore.service.serviceimpl;

import com.bookstore.dao.UserShippingDAO;
import com.bookstore.entity.UserShipping;
import com.bookstore.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {

    @Autowired
    private UserShippingDAO userShippingDAO;

    @Override
    public UserShipping findById(Long shippingAddressId) {
        return userShippingDAO.findById(shippingAddressId).orElse(null);
    }

    @Override
    public void removeById(Long id) {
        userShippingDAO.deleteById(id);
    }
}
