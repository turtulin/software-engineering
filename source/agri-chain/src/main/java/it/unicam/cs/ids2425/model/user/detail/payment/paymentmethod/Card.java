package it.unicam.cs.ids2425.model.user.detail.payment.paymentmethod;

import it.unicam.cs.ids2425.model.user.detail.payment.AbstractPaymentMethod;
import it.unicam.cs.ids2425.model.user.detail.payment.PaymentMethodType;
import jakarta.persistence.Entity;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
public class Card extends AbstractPaymentMethod {
    private String cardNumber;
    private Timestamp expirationDate;
    private String cvv;

    protected Card() {
        super(PaymentMethodType.CARD);
    }

    protected Card(String cardNumber, Timestamp expirationDate, String cvv) {
        super(PaymentMethodType.CARD);
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    @Override
    public void setType(PaymentMethodType type) {
        if (type != PaymentMethodType.CARD) throw new IllegalArgumentException("Invalid type");
        super.setType(PaymentMethodType.CARD);
    }
}
