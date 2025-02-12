package it.unicam.cs.ids2425.users.controller;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.statuses.controller.StatusInfoController;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class GenericUserController implements IController {
    protected final SingletonRepository<List<IUser>, IUser, IUser> userRepository = SingletonRepository.getInstance(new TypeToken<>() {});
    protected final SingletonRepository<Set<Pair<IUser, List<StatusInfo<UserStatus>>>>, Pair<IUser, List<StatusInfo<UserStatus>>>, IUser> userStatusRepository = SingletonRepository.getInstance(new TypeToken<>(){});

    protected final StatusInfoController<UserStatus> statusInfoController = SingletonController.getInstance(new StatusInfoController<UserStatus>() {});

    public List<IUser> getAll(UserStatus status) {
        if (status == null) {
            return userRepository.getAll().stream().filter(u -> check(u, null)).toList();
        }
        return userStatusRepository.getAll().stream()
                .filter(pair -> pair.getValue().getLast().status() == status)
                .map(Pair::getKey).map(userRepository::get)
                .filter( u -> check(u, status)).toList();
    }

    public IUser get(@NonNull IUser u, UserStatus status) {
        check(u, status);
        return userRepository.get(u);
    }

    protected boolean check(@NonNull IUser u, UserStatus status) {
        u = userRepository.get(u);
        if (u == null) {
            throw new IllegalArgumentException("User not found");
        }
        Pair<IUser, List<StatusInfo<UserStatus>>> userStatusPair = userStatusRepository.get(u);
        if (userStatusPair == null) {
            throw new IllegalArgumentException("User status not found");
        }
        if (status != null && userStatusPair.getValue().getLast().status() != status) {
            throw new IllegalArgumentException("User status is not " + status);
        }
        return true;
    }
}
