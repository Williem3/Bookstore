package com.bookstore.utility;

import com.bookstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MailConstructor {

    @Autowired
    private Environment environment;


    // New User email
    public SimpleMailMessage constructResetTokenEmail (String contextPath, Locale locale,
                                                       String token, User user, String password) {
        String url = contextPath + "/newUser?token="+token;
        String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"+password;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Willie's Bookstore - New User");
        email.setText(url + message);

        email.setFrom(environment.getProperty("support.email"));

        System.out.println("EMAIL SENT!");
        return email;
    }


    // Password Reset Email method
    public SimpleMailMessage constructPasswordResetTokenEmail(String contextPath, Locale locale,
                                                              String token, User user, String password) {
        String url = contextPath + "/newUser?token="+token;
        String message = "\nPlease click on this link to reset your password. Your temporary password is: \n"+password;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Willie's Bookstore - Reset Password");
        email.setText(url + message);

        email.setFrom(environment.getProperty("support.email"));

        System.out.println("EMAIL SENT!");
        return email;
    }
}
