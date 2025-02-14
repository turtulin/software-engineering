package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.problems.controller.GenericProblemController;
import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.views.IView;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;

public interface CanReportView extends IView {
    private GenericProblemController getProblemController() {
        return SingletonController.getInstance(new GenericProblemController() {
        });
    }

    default ViewResponse<IProblem> reportProblem(IProblem problem, IUser user) {
        return genericCall(() -> getProblemController().create(problem, user),
                ResponseStatus.CREATED);
    }
}
