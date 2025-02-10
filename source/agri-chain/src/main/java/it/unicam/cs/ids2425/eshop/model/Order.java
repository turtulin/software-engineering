package it.unicam.cs.ids2425.eshop.model;

import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.PaymentMethod;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Order {
    private Cart cart;
    private Address shippingAddress;
    private Address billingAddress;
    private PaymentMethod payment;
    private String trackingNumber;
}
