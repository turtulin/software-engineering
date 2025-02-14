package it.unicam.cs.ids2425.users.model.details.payments.payments;

import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@Data
public class PayPal implements IPaymentMethod {
    private final String email;
    private String password;
}
