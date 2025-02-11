package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class GenericSeller extends GenericUser implements ISeller {
    private List<PaymentMethod> paymentMethods;
    private List<Address> addresses;
}
