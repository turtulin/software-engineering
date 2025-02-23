package it.unicam.cs.ids2425.user.model.detail.payment.paymentmethod;

import it.unicam.cs.ids2425.user.model.detail.payment.AbstractPaymentMethod;
import it.unicam.cs.ids2425.user.model.detail.payment.PaymentMethodType;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayPal extends AbstractPaymentMethod {
    private String email;
    private String password;

    protected PayPal(String email, String password) {
        super(PaymentMethodType.PAYPAL);
        this.email = email;
        this.password = password;
    }

    @Override
    public void setType(PaymentMethodType type) {
        if (type != PaymentMethodType.PAYPAL) throw new IllegalArgumentException("Invalid type");
        super.setType(PaymentMethodType.PAYPAL);
    }
}
