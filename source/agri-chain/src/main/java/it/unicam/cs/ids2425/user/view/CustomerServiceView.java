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
@RequestMapping("/admin/problem")
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

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<ProblemState>> getProblemState(@PathVariable Long id,
                                                                      @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblemState(id, user));
    }

    @RequestMapping(value = "/{id}/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Problem>> getProblem(@PathVariable Long id,
                                                            @PathVariable ProblemStatusCode status,
                                                            @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblem(id, status, user));
    }

    @RequestMapping(value = "/{id}/solve", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> solveProblem(@PathVariable Long id,
                                                              @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.SOLVED, user));
    }

    @RequestMapping(value = "/{id}/reject", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> rejectProblem(@PathVariable Long id,
                                                               @RequestBody JsonNode body,
                                                               @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.REJECTED, reason, user));
    }

    @RequestMapping(value = "/{id}/close", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Problem>> closeProblem(@PathVariable Long id,
                                                              @RequestBody JsonNode body,
                                                              @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.CLOSED, reason, user));
    }
}
