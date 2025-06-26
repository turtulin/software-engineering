package it.unicam.cs.ids2425.model.user.detail.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.detail.payment.paymentmethod.Card;
import it.unicam.cs.ids2425.model.user.detail.payment.paymentmethod.PayPal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Card.class, name = "CARD"),
        @JsonSubTypes.Type(value = PayPal.class, name = "PAYPAL")
})
public abstract class AbstractPaymentMethod implements IPaymentMethod {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private PaymentMethodType type;

    protected AbstractPaymentMethod(PaymentMethodType type) {
        this.type = type;
    }
}
