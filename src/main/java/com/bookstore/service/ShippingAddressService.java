package com.bookstore.service;

import com.bookstore.entity.ShippingAddress;
import com.bookstore.entity.UserShipping;

public interface ShippingAddressService {

    ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
