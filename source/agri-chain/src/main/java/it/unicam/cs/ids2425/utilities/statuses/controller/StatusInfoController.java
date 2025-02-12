package it.unicam.cs.ids2425.utilities.statuses.controller;

import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.IStatus;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class StatusInfoController<T extends IStatus> implements IController {
    private final SingletonRepository<List<StatusInfo<T>>, StatusInfo<T>, StatusInfo<T>> statusInfoRepository = SingletonRepository.getInstance(new TypeToken<>() {});

    private final GenericUserController userController = SingletonController.getInstance(new GenericUserController() {});

    public StatusInfo<T> create(@NonNull StatusInfo<T> statusInfo, @NonNull IUser user) {
        user = userController.get(user, UserStatus.ACTIVE);
        if (!statusInfo.user().equals(user)) {
            statusInfo = new StatusInfo<>(statusInfo.status(), user, statusInfo.message());
        }
        statusInfoRepository.create(statusInfo);
        return statusInfoRepository.get(statusInfo);
    }
}
