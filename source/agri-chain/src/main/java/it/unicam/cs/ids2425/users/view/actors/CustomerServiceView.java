package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.problems.controller.GenericProblemController;
import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.CustomerServiceController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.CanReportView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CustomerServiceView extends GenericUserView implements CanRegisterView, CanReportView {
    private final GenericProblemController problemController = SingletonController.getInstance(new GenericProblemController() {
    });

    public ViewResponse<List<IProblem>> getAll(IUser user) {
        return genericCall(() -> {
                    List<IProblem> problems = problemController.getAll(user, null);
                    return problems.isEmpty() ? null : problems;
                },
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IProblem> get(IProblem problem, IUser user) {
        return genericCall(() -> problemController.get(problem, user),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IProblem> solve(IProblem problem, IUser user) {
        return genericCall(() -> problemController.solve(problem, user),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IProblem> reject(IProblem problem, IUser user) {
        return genericCall(() -> problemController.reject(problem, user),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IProblem> close(IProblem problem, IUser user) {
        return genericCall(() -> problemController.close(problem, user),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }


    @Override
    public CanRegisterController getCanRegisterController() {
        return SingletonController.getInstance(new CustomerServiceController() {
        });
    }
}
