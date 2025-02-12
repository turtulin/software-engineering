package it.unicam.cs.ids2425.users.controller.details;

import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class AddressController implements IController {
    private final SingletonRepository<List<Address>, Address, Address> addressRepository = SingletonRepository.getInstance(new TypeToken<>() {});

    public @NonNull Address get(@NonNull Address shippingAddress) {
        return addressRepository.get(shippingAddress);
    }
}
