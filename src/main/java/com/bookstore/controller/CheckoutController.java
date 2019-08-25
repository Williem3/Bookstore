package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.service.*;
import com.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class CheckoutController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserShippingService userShippingService;
    @Autowired
    private UserPaymentService userPaymentService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BillingAddressService billingAddressService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired ShoppingCartService shoppingCartService;
    @Autowired
    private OrderService orderService;


    private ShippingAddress shippingAddress = new ShippingAddress();
    private BillingAddress billingAddress = new BillingAddress();
    private Payment payment = new Payment();


    @RequestMapping("/checkout")
    public String checkout(@RequestParam("id") Long cartId,
                           @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField,
                           Model theModel, Principal principal) {

        User user = userService.findByUsername(principal.getName());

        if (cartId != user.getShoppingCart().getId()) {
            return "badRequestPage";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        if (cartItemList.size() == 0) {
            theModel.addAttribute("emptyCart", true);
            return "forward:/shoppingCart/cart";
        }

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
                theModel.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();

        theModel.addAttribute("userShippingList", userShippingList);
        theModel.addAttribute("userPaymentList", userPaymentList);

        if (userPaymentList.size() == 0) {
            theModel.addAttribute("emptyPaymentList", true);
        } else {
            theModel.addAttribute("emptyPaymentList", false);
        }
        if (userShippingList.size() == 0) {
            theModel.addAttribute("emptyShippingList", true);
        } else {
            theModel.addAttribute("emptyShippingList", false);
        }


        ShoppingCart shoppingCart = user.getShoppingCart();

        for (UserShipping userShipping : userShippingList) {
            if (userShipping.isUserShippingDefault()) {
                shippingAddressService.setByUserShipping(userShipping, shippingAddress);
            }
        }

        for (UserPayment userPayment : userPaymentList) {
            if (userPayment.isDefaultPayment()) {
                paymentService.setByUserPayment(userPayment, payment);
                billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);

            }
        }

        theModel.addAttribute("shippingAddress", shippingAddress);
        theModel.addAttribute("payment", payment);
        theModel.addAttribute("billingAddress", billingAddress);
        theModel.addAttribute("cartItemList", cartItemList);
        theModel.addAttribute("shoppingCart", shoppingCart);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        theModel.addAttribute("stateList", stateList);

        theModel.addAttribute("classActiveShipping", true);

        if(missingRequiredField) {
            theModel.addAttribute("missingRequiredField", true);
        }

        return "checkout";
    }

    @RequestMapping("/setShippingAddress")
    public String setShippingAddress(
            @RequestParam("userShippingId") Long userShippingId,
            Principal principal, Model theModel) {
        User user = userService.findByUsername(principal.getName());

        UserShipping userShipping = userShippingService.findById(userShippingId);

        if(userShipping.getUser().getId() != user.getId()) {
            return "badRequestPage";
        }
        else {
            shippingAddressService.setByUserShipping(userShipping, shippingAddress);
            List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

            BillingAddress billingAddress = new BillingAddress();

            theModel.addAttribute("shippingAddress", shippingAddress);
            theModel.addAttribute("payment", payment);
            theModel.addAttribute("billingAddress", billingAddress);
            theModel.addAttribute("cartItemList", cartItemList);
            theModel.addAttribute("shoppingCart", user.getShoppingCart());

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            theModel.addAttribute("stateList", stateList);


            List<UserShipping> userShippingList = user.getUserShippingList();
            List<UserPayment> userPaymentList = user.getUserPaymentList();

            theModel.addAttribute("userShippingList", userShippingList);
            theModel.addAttribute("userPaymentList", userPaymentList);

            theModel.addAttribute("shippingAddress", shippingAddress);
            theModel.addAttribute("classActiveShipping", true);

            if (userPaymentList.size() == 0) {
                theModel.addAttribute("emptyPaymentList", true);
            } else {
                theModel.addAttribute("emptyPaymentList", false);
            }

            theModel.addAttribute("emptyShippingList", false);

        }

        return "checkout";
    }

    @RequestMapping("/setPaymentMethod")
    public String setPaymentMethod(
            @RequestParam("userPaymentId") Long userPaymentId,
            Principal principal, Model theModel) {

        User user = userService.findByUsername(principal.getName());

        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        UserBilling userBilling = userPayment.getUserBilling();

        if (userPayment.getUser().getId() != user.getId()) {
            return "badRequestPage";
        } else {
            paymentService.setByUserPayment(userPayment, payment);
            List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

            billingAddressService.setByUserBilling(userBilling, billingAddress);

            theModel.addAttribute("shippingAddress", shippingAddress);
            theModel.addAttribute("payment", payment);
            theModel.addAttribute("billingAddress", billingAddress);
            theModel.addAttribute("cartItemList", cartItemList);
            theModel.addAttribute("shoppingCart", user.getShoppingCart());

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            theModel.addAttribute("stateList", stateList);


            List<UserShipping> userShippingList = user.getUserShippingList();
            List<UserPayment> userPaymentList = user.getUserPaymentList();

            theModel.addAttribute("userShippingList", userShippingList);
            theModel.addAttribute("userPaymentList", userPaymentList);

            theModel.addAttribute("shippingAddress", shippingAddress);
            theModel.addAttribute("classActivePayment", true);


            theModel.addAttribute("emptyPaymentList", false);

            if (userShippingList.size() == 0) {
                theModel.addAttribute("emptyShippingList", true);
            } else {
                theModel.addAttribute("emptyShippingList", false);
            }
        }

        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkoutPost(
            @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
            @ModelAttribute("billingAddress") BillingAddress billingAddress,
            @ModelAttribute("payment") Payment payment,
            @ModelAttribute("billingSameAsShipping") String  billingSameAsShipping,
            @ModelAttribute("shippingMethod") String shippingMethod,
            Principal principal,
            Model theModel) {
        ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        theModel.addAttribute("cartItemList", cartItemList);

        if (billingSameAsShipping.equals("true")) {
            billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
            billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
            billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
            billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
            billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
            billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
            billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
        }

        if (shippingAddress.getShippingAddressStreet1().isEmpty() ||
                shippingAddress.getShippingAddressCity().isEmpty() ||
                shippingAddress.getShippingAddressState().isEmpty() ||
                shippingAddress.getShippingAddressName().isEmpty() ||
                shippingAddress.getShippingAddressZipcode().isEmpty() ||
                payment.getCardNumber().isEmpty() ||
                payment.getCvc() == 0 ||
                billingAddress.getBillingAddressStreet1().isEmpty() ||
                billingAddress.getBillingAddressCity().isEmpty() ||
                billingAddress.getBillingAddressName().isEmpty() ||
                billingAddress.getBillingAddressZipcode().isEmpty()) {

            return "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";
        }

        User user = userService.findByUsername(principal.getName());
        Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);

        mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

        shoppingCartService.clearShoppingCart(shoppingCart);

        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate;

        if (shippingMethod.equals("groundShipping")) {
            estimatedDeliveryDate = today.plusDays(5);
        } else {
            estimatedDeliveryDate = today.plusDays(3);
        }

        theModel.addAttribute("estimateDeliveryDate", estimatedDeliveryDate);

        return "orderSubmittedPage";
    }
}
