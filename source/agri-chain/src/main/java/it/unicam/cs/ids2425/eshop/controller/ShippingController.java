package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import lombok.NonNull;

public class ShippingController implements IController {

    public String getTrackingNumber(@NonNull Cart cart, @NonNull Address shippingAddress) {
        return Long.toHexString(cart.hashCode() + shippingAddress.hashCode() + System.currentTimeMillis());
    }
}
