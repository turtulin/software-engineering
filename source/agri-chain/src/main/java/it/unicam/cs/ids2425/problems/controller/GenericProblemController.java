package it.unicam.cs.ids2425.problems.controller;

import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.controller.actors.CustomerServiceController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.CustomerService;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ProblemStatus;
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
public class GenericProblemController implements IController {

    private final SingletonRepository<List<IProblem>, IProblem, IProblem> problemRepository = SingletonRepository.getInstance(new TypeToken<>() {});
    private final SingletonRepository<Set<Pair<IProblem, List<StatusInfo<ProblemStatus>>>>, Pair<IProblem, List<StatusInfo<ProblemStatus>>>, IProblem> problemStatusRepository = SingletonRepository.getInstance(new TypeToken<>(){});

    private final StatusInfoController<ProblemStatus> statusInfoController = SingletonController.getInstance(new StatusInfoController<>() {});
    private final CustomerServiceController customerServiceController = SingletonController.getInstance(new CustomerServiceController() {});

    public IProblem create(@NonNull IProblem p, @NonNull IUser u) {
        u = SingletonController.getInstance(new GenericUserController() {}).get(u, UserStatus.ACTIVE);

        if (p.getDescription() == null || p.getDescription().isBlank()) {
            throw new IllegalArgumentException("Problem description is empty");
        }

        p.setUser(u);
        problemRepository.create(p);

        StatusInfo<ProblemStatus> problemStatusStatusInfo = statusInfoController.create(new StatusInfo<>(ProblemStatus.OPEN, u), u);

        Pair<IProblem, List<StatusInfo<ProblemStatus>>> problemStatusPair = problemStatusRepository.get(p);
        if (problemStatusPair == null) {
            problemStatusPair = new Pair<>(p, List.of(problemStatusStatusInfo));
            problemStatusRepository.create(problemStatusPair);
            return problemRepository.get(problemStatusPair.getKey());
        }

        problemStatusPair.getValue().add(problemStatusStatusInfo);
        problemStatusRepository.save(p, problemStatusPair);
        return problemRepository.get(problemStatusPair.getKey());
    }

    private List<IProblem> getAllProblems(ProblemStatus status){
        if (status == null){
            return problemRepository.getAll();
        }
        return problemStatusRepository.getAll().stream().sorted()
                .filter(pair -> pair.getValue().getLast().status() == status)
                .map(Pair::getKey).map(problemRepository::get).toList();
    }

    public List<IProblem> getAll(@NonNull IUser user, ProblemStatus status) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        return getAllProblems(status).stream().map(problemStatusRepository::get)
                .filter(pair -> pair.getValue().getLast().user().equals(customerService))
                .map(Pair::getKey).toList();
    }

    public IProblem get(IProblem problem, IUser user) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        Pair<IProblem, List<StatusInfo<ProblemStatus>>> problemStatusPair = problemStatusRepository.get(problem);
        if (problemStatusPair == null) {
            return null;
        }
        if (problemStatusPair.getValue().stream().sorted().toList().getLast().user().equals(customerService)){
            return problemStatusPair.getKey();
        }
        return null;
    }

    public IProblem solve(IProblem problem, IUser user) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        problem = get(problem, user);
        if (problem == null) {
            return null;
        }
        StatusInfo<ProblemStatus> problemStatusStatusInfo = statusInfoController.create(new StatusInfo<>(ProblemStatus.RESOLVED, customerService), customerService);
        Pair<IProblem, List<StatusInfo<ProblemStatus>>> problemStatusPair = problemStatusRepository.get(problem);
        problemStatusPair.getValue().add(problemStatusStatusInfo);
        problemStatusRepository.save(problem, problemStatusPair);
        return problemRepository.get(problem);
    }

    public IProblem reject(IProblem problem, IUser user) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        problem = get(problem, user);
        if (problem == null) {
            return null;
        }
        StatusInfo<ProblemStatus> problemStatusStatusInfo = statusInfoController.create(new StatusInfo<>(ProblemStatus.UNRESOLVED, customerService), customerService);
        Pair<IProblem, List<StatusInfo<ProblemStatus>>> problemStatusPair = problemStatusRepository.get(problem);
        problemStatusPair.getValue().add(problemStatusStatusInfo);
        problemStatusRepository.save(problem, problemStatusPair);
        return problemRepository.get(problem);
    }

    public IProblem close(IProblem problem, IUser user) {
        IUser customerService = customerServiceController.get(user, UserStatus.ACTIVE);
        problem = get(problem, user);
        if (problem == null) {
            return null;
        }
        StatusInfo<ProblemStatus> problemStatusStatusInfo = statusInfoController.create(new StatusInfo<>(ProblemStatus.CLOSED, customerService), customerService);
        Pair<IProblem, List<StatusInfo<ProblemStatus>>> problemStatusPair = problemStatusRepository.get(problem);
        problemStatusPair.getValue().add(problemStatusStatusInfo);
        problemStatusRepository.save(problem, problemStatusPair);
        return problemRepository.get(problem);
    }
}
