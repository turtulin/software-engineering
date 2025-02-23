package it.unicam.cs.ids2425.user.view;

import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.problem.model.Problem;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ICanReportView extends IView {
    ProblemController getProblemController();

    @RequestMapping(path = "/report", method = {RequestMethod.POST})
    default ResponseEntity<ViewResponse<Problem>> reportProblem(@RequestBody Problem problem, @RequestAttribute("user") User user) {
        return genericCall(() -> getProblemController().create(problem, user),
                HttpStatus.CREATED);
    }
}
