package it.unicam.cs.ids2425.users.model.details.payments.payments;

import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;

import java.sql.Timestamp;
import java.util.Objects;


public record Card(String cardNumber, Timestamp expirationDate, String cvv) implements IPaymentMethod {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cvv(), card.cvv()) && Objects.equals(cardNumber(), card.cardNumber()) && Objects.equals(expirationDate(), card.expirationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber(), expirationDate(), cvv());
    }
}
