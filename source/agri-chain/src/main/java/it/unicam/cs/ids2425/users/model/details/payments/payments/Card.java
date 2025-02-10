package it.unicam.cs.ids2425.users.model.details.payments.payments;

import it.unicam.cs.ids2425.users.model.details.payments.PaymentMethod;

import java.sql.Timestamp;

public class Card extends PaymentMethod {
    private String cardNumber;
    private Timestamp expirationDate;
    private String cvv;
}
