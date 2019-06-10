package com.bookstore.service.serviceimpl;

import com.bookstore.dao.PasswordResetTokenDAO;
import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.entity.User;
import com.bookstore.entity.UserBilling;
import com.bookstore.entity.UserPayment;
import com.bookstore.entity.security.PasswordResetToken;
import com.bookstore.entity.security.UserRole;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenDAO.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenDAO.save(myToken);
    }
    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception{
        User localUser = userDAO.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("user already exists. Nothing will be done");
        } else {
            for (UserRole ur : userRoles) {
                roleDAO.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            localUser = userDAO.save(user);
        }
        return localUser;
    }
    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {

        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
        userDAO.save(user);
    }
}
