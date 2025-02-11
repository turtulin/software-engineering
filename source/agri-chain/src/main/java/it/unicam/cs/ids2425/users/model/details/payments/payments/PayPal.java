package it.unicam.cs.ids2425.users.model.details.payments.payments;

import it.unicam.cs.ids2425.users.model.details.payments.PaymentMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PayPal extends PaymentMethod {
    private String email;
    private String password;
}
