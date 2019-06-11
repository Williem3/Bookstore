package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.entity.UserBilling;
import com.bookstore.entity.UserPayment;
import com.bookstore.entity.UserShipping;
import com.bookstore.entity.security.PasswordResetToken;
import com.bookstore.entity.security.UserRole;

import java.util.Set;

public interface UserService {

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

    void setUserDefaultPayment(Long userPaymentId, User user);

    void updateUserShipping(UserShipping userShipping, User user);

    void setUserDefaultShipping(Long userShippingId, User user);
}
