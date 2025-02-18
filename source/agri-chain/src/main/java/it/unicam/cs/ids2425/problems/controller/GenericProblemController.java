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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
public class GenericProblemController implements IController {

    private final SingletonRepository<IProblem> problemRepository = SingletonRepository.getInstance(IProblem.class);
    private final SingletonRepository<ProblemState> problemStatusRepository = SingletonRepository.getInstance(ProblemState.class);

    public IProblem create(@NonNull IProblem problem, @NonNull IUser user) {
        user = SingletonController.getInstance(new GenericUserController()).get(user, UserStatus.ACTIVE);

        if (problem.getDescription() == null || problem.getDescription().isBlank()) {
            throw new IllegalArgumentException("Problem description is empty");
        }

        problem.setUser(user);
        ProblemState problemStatus = new ProblemState(problem, ProblemStatus.OPEN, user);
        problemRepository.save(problem);
        problemStatusRepository.save(problemStatus);
        Optional<IProblem> p = problemRepository.findById(problem);
        if (p.isPresent()) {
            return p.get();
        }
        throw new RuntimeException("Problem not found");
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
        SingletonController.getInstance(new CustomerServiceController()).get(user, UserStatus.ACTIVE);
        return getAllProblems(status);
    }

    public IProblem get(@NonNull IProblem problem, @NonNull IUser user) {
        SingletonController.getInstance(new CustomerServiceController()).get(user, UserStatus.ACTIVE);
        return problemStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(problem))
                .map(state -> problemRepository.findById(state.getEntity()).orElseThrow(() -> new RuntimeException("Problem not found")))
                .findFirst().orElse(null);
    }

    private IProblem updateProblem(IProblem problem, IUser user, ProblemStatus newStatus) {
        IProblem p = get(problem, user);
        if (p == null) {
            return null;
        }
        ProblemState oldState = problemStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(p))
                .findFirst().orElse(null);
        ProblemState problemStatus = new ProblemState(p, newStatus, user, oldState);
        problemStatusRepository.save(problemStatus);
        Optional<IProblem> optionalProblem = problemRepository.findById(p);
        if (optionalProblem.isPresent()) {
            return optionalProblem.get();
        }
        throw new RuntimeException("Problem not found");
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

    public ProblemStatus getStatus(@NonNull IProblem problem, @NonNull IUser user) {
        SingletonController.getInstance(new CustomerServiceController()).get(user, UserStatus.ACTIVE);
        IProblem p = get(problem, user);
        if (p == null) {
            throw new NoSuchElementException("Problem not found");
        }
        return problemStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(p))
                .map(ProblemState::getStatus).sorted().toList().getLast();
    }
}
