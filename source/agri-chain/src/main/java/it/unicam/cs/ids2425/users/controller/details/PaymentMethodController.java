package it.unicam.cs.ids2425.users.controller.details;

import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NonNull;

import java.util.List;

public class PaymentMethodController implements IController {
    private final SingletonRepository<List<IPaymentMethod>, IPaymentMethod, IPaymentMethod> paymentMethodRepository = SingletonRepository.getInstance(new TypeToken<>() {
    });

    public @NonNull IPaymentMethod get(@NonNull IPaymentMethod payment) {
        return paymentMethodRepository.get(payment);
    }
}
