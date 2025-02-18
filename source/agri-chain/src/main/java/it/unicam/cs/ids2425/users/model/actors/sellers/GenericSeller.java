package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public sealed abstract class GenericSeller extends GenericUser implements ISeller permits Producer, Transformer, Distributor, EventPlanner {
    private List<IPaymentMethod> paymentMethods;
    private List<Address> addresses;

    public GenericSeller(String username, String password) {
        super(username, password);
        paymentMethods = List.of();
        addresses = List.of();
    }
}
