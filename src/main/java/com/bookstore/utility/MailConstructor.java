package com.bookstore.utility;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class MailConstructor {

    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;


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

    public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale local) {
        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("user", user);
        context.setVariable("cartItemList", order.getCartItemList());

        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);

                email.setTo(user.getEmail());
                email.setSubject("Order Confirmation - " + order.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("dev.wmangram@gmail.com"));
            }
        }
    }
}
