package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.entity.security.PasswordResetToken;
import com.bookstore.entity.security.Role;
import com.bookstore.entity.security.UserRole;
import com.bookstore.service.BookService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.service.serviceimpl.UserSecurityService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import com.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/myAccount")
    public String myAccount() {
        return "myAccount";
    }

    @RequestMapping("/login")
    public String login(Model theModel) {
        theModel.addAttribute("classActiveLogin", true );
        return "myAccount";
    }
    @GetMapping("/myProfile")
    public String myProfile(Model theModel, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        theModel.addAttribute("user", user);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());
//        theModel.addAttribute("orderList", user.getUserOrderList());
        UserShipping userShipping = new UserShipping();
        theModel.addAttribute("userShipping", userShipping);

        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);

        theModel.addAttribute("stateList", stateList);
        theModel.addAttribute("classActiveEdit", true);

        return "myProfile";
    }

    @GetMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(Model theModel, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());

        theModel.addAttribute("user", user);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());
//        theModel.addAttribute("orderList", user.orderList());

        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("classActiveShipping", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @GetMapping("/addNewShippingAddress")
    public String addNewShippingAddress(Model theModel, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        theModel.addAttribute("user", user);

        theModel.addAttribute("addNewShippingAddress", true);
        theModel.addAttribute("classActiveShipping", true);
        theModel.addAttribute("listOfCreditCards", true);

        UserShipping userShipping = new UserShipping();
        theModel.addAttribute("userShipping", userShipping);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        theModel.addAttribute("stateList", stateList);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());
//        theModel.addAttribute("orderList", user.orderList());

        return "myProfile";
    }

    @PostMapping("/addNewShippingAddress")
    public String addNewShippingAddressPost(@ModelAttribute("userShipping") UserShipping userShipping,
                                            Principal principal, Model theModel) {

        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        theModel.addAttribute("user", user);
        theModel.addAttribute("userShippingList", user.getUserShippingList());
        theModel.addAttribute("classActiveShipping", true);
        theModel.addAttribute("listOfShippingAddresses", true);
        return "myProfile";
    }

    @GetMapping("/updateShippingAddress")
    public String updateShippingAddress(@ModelAttribute("id") Long shippingAddressId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());

        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if(user.getId() != userShipping.getUser().getId()) {
            return "badRequest";
        }
        else {
            theModel.addAttribute("user", user);
            theModel.addAttribute("userShipping", userShipping);

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);

            theModel.addAttribute("stateList", stateList);

            theModel.addAttribute("addNewShippingAddress", true);
            theModel.addAttribute("classActiveShipping", true);
            theModel.addAttribute("listOfCreditCards", true);

            theModel.addAttribute("userPaymentList", user.getUserPaymentList());
            theModel.addAttribute("userShippingList", user.getUserShippingList());
        }

        return "myProfile";
    }

    @PostMapping("/setDefaultShippingAddress")
    public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);

        theModel.addAttribute("user", user);
        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("classActiveShipping", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());

        return "myProfile";
    }

    @GetMapping("/removeShippingAddress")
    public String removeShippingAddress(@ModelAttribute("id") Long userShippingId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());

        UserShipping userShipping = userShippingService.findById(userShippingId);

        if(user.getId() != userShipping.getUser().getId()) {
            return "badRequest";
        }
        else {
            theModel.addAttribute("user", user);

            userShippingService.removeById(userShippingId);

            theModel.addAttribute("listOfShippingAddresses", true);
            theModel.addAttribute("classActiveShipping", true);

            theModel.addAttribute("userPaymentList", user.getUserPaymentList());
            theModel.addAttribute("userShippingList", user.getUserShippingList());
        }

        return "myProfile";
    }

    @GetMapping("/listOfCreditCards")
    public String listOfCreditCards(Model theModel, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());

        theModel.addAttribute("user", user);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());
//        theModel.addAttribute("orderList", user.orderList());

        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("classActiveBilling", true);
        theModel.addAttribute("listOfShippingAddresses", true);
        return "myProfile";
    }


    @GetMapping("/addNewCreditCard")
    public String addNewCreditCard(Model theModel, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        theModel.addAttribute("user", user);

        theModel.addAttribute("addNewCreditCard", true);
        theModel.addAttribute("classActiveBilling", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();
        theModel.addAttribute("userBilling", userBilling);
        theModel.addAttribute("userPayment", userPayment);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        theModel.addAttribute("stateList", stateList);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());

//        theModel.addAttribute("orderList", user.orderList());

        return "myProfile";
    }

    @PostMapping("/addNewCreditCard")
    public String addNewCreditCardPost(@ModelAttribute("userPayment") UserPayment userPayment,
                                       @ModelAttribute ("userBilling") UserBilling userBilling,
                                       Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        theModel.addAttribute("user", user);
        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());
        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("classActiveBilling", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @GetMapping("/updateCreditCard")
    public String updateCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());

        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "badRequest";
        }
        else {
            theModel.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            theModel.addAttribute("userPayment", userPayment);
            theModel.addAttribute("userBilling", userBilling);

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);

            theModel.addAttribute("stateList", stateList);

            theModel.addAttribute("addNewCreditCard", true);
            theModel.addAttribute("classActiveBilling", true);
            theModel.addAttribute("listOfShippingAddresses", true);

            theModel.addAttribute("userPaymentList", user.getUserPaymentList());
            theModel.addAttribute("userShippingList", user.getUserShippingList());

            System.out.println("\\\\nThis is the saved payment type, expiryMonth, expiryYear, and CVC" + userPayment.getType() + userPayment.getExpiryMonth() + userPayment.getExpiryYear() + userPayment.getCvc());
        }

        return "myProfile";
    }

    @PostMapping("/setDefaultPayment")
    public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultPaymentId, user);

        theModel.addAttribute("user", user);
        theModel.addAttribute("listOfCreditCards", true);
        theModel.addAttribute("classActiveBilling", true);
        theModel.addAttribute("listOfShippingAddresses", true);

        theModel.addAttribute("userPaymentList", user.getUserPaymentList());
        theModel.addAttribute("userShippingList", user.getUserShippingList());

        return "myProfile";
    }

    @GetMapping("/removeCreditCard")
    public String removeCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "badRequest";
        }
        else {
            theModel.addAttribute("user", user);
            userPaymentService.removeById(creditCardId);

            theModel.addAttribute("listOfCreditCards", true);
            theModel.addAttribute("classActiveBilling", true);
            theModel.addAttribute("listOfShippingAddresses", true);

            theModel.addAttribute("userPaymentList", user.getUserPaymentList());
            theModel.addAttribute("userShippingList", user.getUserShippingList());
        }

        return "myProfile";
    }


    @RequestMapping("/forgetPassword")
    public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email,
                                 @ModelAttribute("username") String username,
                                 Model theModel) {
        theModel.addAttribute("classActiveForgetPassword", true );

        User user = userService.findByEmail(email);

        if (user == null) {
            theModel.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);
        // create token for user to reset password
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        SimpleMailMessage newEmail = mailConstructor.constructPasswordResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        // send email
        mailSender.send(newEmail);

        theModel.addAttribute("forgetPasswordEmailSent", "true");


        return "myAccount";
    }

    // New User Logic
    @GetMapping("/newUser")
    public String newUser(Locale locale, @RequestParam("token") String token,
            Model theModel) {

        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid token.";
            theModel.addAttribute("message", message);

            return "redirect:/badRequest";
        }

        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        theModel.addAttribute("classActiveNewUser", true );
        theModel.addAttribute("user", user);
        return "myProfile";
    }

    @PostMapping("/newUser")
    public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
                              @ModelAttribute("username") String username,
                              Model theModel) throws Exception {

        theModel.addAttribute("classActiveNewAccount", true);
        theModel.addAttribute("email", userEmail);
        theModel.addAttribute("username", username);

        if (userService.findByUsername(username) != null) {
            theModel.addAttribute("usernameExists", true);

            return "myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            theModel.addAttribute("emailExists", true);

            return "myAccount";
        }

        // create password with a default encrypted password
        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);
        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        // set user roles
        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);


        // create token for user to reset password
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        // send email
        mailSender.send(email);

        theModel.addAttribute("emailSent", "true");


        return "myAccount";
    }

    // Bookshelf
    @GetMapping("/bookshelf")
    public String bookshelf(Model model) {
        List<Book> bookList = bookService.findAll();

        model.addAttribute("bookList", bookList);
        return "bookShelf";
    }

    @GetMapping("/bookDetail")
    public String bookDetail(@PathParam("id") Long id, Model theModel, Principal principal) {

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            theModel.addAttribute("user", user);
        }
        Book book = bookService.findOne(id);

        theModel.addAttribute("book", book);

        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        theModel.addAttribute("qtyList", qtyList);
        theModel.addAttribute("qty", 1);

        return "bookDetail";
    }
}
