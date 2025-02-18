package it.unicam.cs.ids2425.problems.controller;

import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.problems.model.ProblemState;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.controller.actors.CustomerServiceController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ProblemStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class GenericProblemController implements IController {

    private final SingletonRepository<IProblem> problemRepository = SingletonRepository.getInstance(IProblem.class);
    private final SingletonRepository<ProblemState> problemStatusRepository = SingletonRepository.getInstance(ProblemState.class);

    private final CustomerServiceController customerServiceController = SingletonController.getInstance(new CustomerServiceController() {
    });

    public IProblem create(@NonNull IProblem p, @NonNull IUser u) {
        u = SingletonController.getInstance(new GenericUserController() {
        }).get(u, UserStatus.ACTIVE);

        if (p.getDescription() == null || p.getDescription().isBlank()) {
            throw new IllegalArgumentException("Problem description is empty");
        }

        p.setUser(u);
        ProblemState problemStatus = new ProblemState(p, ProblemStatus.OPEN, u);
        problemRepository.save(p);
        problemStatusRepository.save(problemStatus);
        return problemRepository.findById(p).get();
    }

    private List<IProblem> getAllProblems(ProblemStatus status) {
        if (status == null) {
            return problemRepository.findAll();
        }
        return problemStatusRepository.findAll().stream().sorted()
                .filter(state -> state.getStatus() == status)
                .map(ProblemState::getEntity)
                .map(entity -> problemRepository.findById(entity).orElse(null))
                .filter(Objects::nonNull).toList();
    }

    public List<IProblem> getAll(@NonNull IUser user, ProblemStatus status) {
        customerServiceController.get(user, UserStatus.ACTIVE);
        return getAllProblems(status);
    }

    public IProblem get(IProblem problem, IUser user) {
        customerServiceController.get(user, UserStatus.ACTIVE);

        return problemStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(problem))
                .map(state -> problemRepository.findById(state.getEntity()).get())
                .findFirst().orElse(null);
    }

    private IProblem updateProblem(IProblem problem, IUser user, ProblemStatus newStatus) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        IProblem p = get(problem, user);
        if (p == null) {
            return null;
        }
        ProblemState oldState = problemStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(p))
                .findFirst().orElse(null);
        ProblemState problemStatus = new ProblemState(p, newStatus, customerService, oldState);
        problemStatusRepository.save(problemStatus);
        return problemRepository.findById(problem).get();
    }

    public IProblem solve(IProblem problem, IUser user) {
        return updateProblem(problem, user, ProblemStatus.RESOLVED);
    }

    public IProblem reject(IProblem problem, IUser user) {
        return updateProblem(problem, user, ProblemStatus.UNRESOLVED);
    }

    public IProblem close(IProblem problem, IUser user) {
        return updateProblem(problem, user, ProblemStatus.CLOSED);
    }
}
