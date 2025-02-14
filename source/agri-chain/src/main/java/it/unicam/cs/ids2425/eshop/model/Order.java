package it.unicam.cs.ids2425.eshop.model;

import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"cart", "shippingAddress", "billingAddress", "payment"})
public class Order {
    private Cart cart;
    private Address shippingAddress;
    private Address billingAddress;
    private IPaymentMethod payment;
    private String trackingNumber;
}
