package com.bookstore.service;

import com.bookstore.entity.BillingAddress;
import com.bookstore.entity.UserBilling;

public interface BillingAddressService {

    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
