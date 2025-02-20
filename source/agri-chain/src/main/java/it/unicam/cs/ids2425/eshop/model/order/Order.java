package it.unicam.cs.ids2425.eshop.model.order;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"id"})
public class Order implements Identifiable<Long> {
    private static long lastId = 0L;
    private final Long id;
    private final Cart cart;
    private final Address shippingAddress;
    private final Address billingAddress;
    private final IPaymentMethod payment;
    private final String trackingNumber;

    public Order(Cart cart, Address shippingAddress, Address billingAddress, IPaymentMethod payment, String trackingNumber) {
        id = ++lastId;
        this.cart = cart;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.payment = payment;
        this.trackingNumber = trackingNumber;
    }
}
