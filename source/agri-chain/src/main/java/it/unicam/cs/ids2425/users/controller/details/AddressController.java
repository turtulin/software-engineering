package it.unicam.cs.ids2425.users.controller.details;

import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;

@NoArgsConstructor
public class AddressController implements IController {
    private final SingletonRepository<Address> addressRepository = SingletonRepository.getInstance(Address.class);

    public @NonNull Address get(@NonNull Address shippingAddress) {
        return addressRepository.findById(shippingAddress).orElseThrow(
                () -> new NoSuchElementException("Address not found"));
    }
}
