package it.unicam.cs.ids2425.user.view;

import com.fasterxml.jackson.databind.JsonNode;
import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.problem.model.Problem;
import it.unicam.cs.ids2425.problem.model.ProblemState;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.controller.actor.UserController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ProblemStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@RestController
@RequestMapping("/customer-service")
public class CustomerServiceView implements IView, ICanLogoutView, ICanRegisterView, ICanReportView {
    private final OtherUserController logoutController;
    private final UserController registerController;
    private final ProblemController problemController;

    @Autowired
    public CustomerServiceView(OtherUserController otherUserController, ProblemController problemController) {
        this.logoutController = otherUserController;
        this.registerController = otherUserController;
        this.problemController = problemController;
    }

    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Problem>>> getAllProblems(@RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getAllProblems(user));
    }

    @RequestMapping(value = "/all/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Problem>>> getAllProblems(@PathVariable String status,
                                                                      @RequestAttribute("user") User user) {
        ProblemStatusCode statusCode = ProblemStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> problemController.getAllProblems(user, statusCode));
    }

    @RequestMapping(value = "/{problemId}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<ProblemState>> getProblemState(@PathVariable Long problemId,
                                                                      @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblemState(problemId, user));
    }

    @RequestMapping(value = "/{problemId}/{statusCode}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Problem>> getProblem(@PathVariable Long problemId,
                                                            @PathVariable ProblemStatusCode statusCode,
                                                            @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblem(problemId, statusCode, user));
    }

    @RequestMapping(value = "/{problemId}/solve", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> solveProblem(@PathVariable Long problemId,
                                                              @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.updateProblem(problemId, ProblemStatusCode.SOLVED, user));
    }

    @RequestMapping(value = "/{problemId}/reject", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> rejectProblem(@PathVariable Long problemId,
                                                               @RequestBody JsonNode body,
                                                               @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(problemId, ProblemStatusCode.REJECTED, reason, user));
    }

    @RequestMapping(value = "/{problemId}/close", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> closeProblem(@PathVariable Long problemId,
                                                              @RequestBody JsonNode body,
                                                              @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(problemId, ProblemStatusCode.CLOSED, reason, user));
    }
}
