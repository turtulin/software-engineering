package it.unicam.cs.ids2425.users.controller.details;

import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NonNull;

import java.util.NoSuchElementException;

public class PaymentMethodController implements IController {
    private final SingletonRepository<IPaymentMethod> paymentMethodRepository = SingletonRepository.getInstance(IPaymentMethod.class);

    public @NonNull IPaymentMethod get(@NonNull IPaymentMethod payment) {
        return paymentMethodRepository.findById(payment).orElseThrow(() -> new NoSuchElementException("Payment method not found"));
    }
}
