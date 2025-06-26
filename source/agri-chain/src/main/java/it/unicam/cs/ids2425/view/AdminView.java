package it.unicam.cs.ids2425.view;

import com.fasterxml.jackson.databind.JsonNode;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.model.problem.Problem;
import it.unicam.cs.ids2425.model.problem.ProblemState;
import it.unicam.cs.ids2425.controller.user.AdminController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ProblemStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@RestController
@RequestMapping("/admin")
public class AdminView implements IView, ICanLogoutView, ICanRegisterView {
    private final OtherUserController logoutController;
    private final AdminController registerController;
    private final ProblemController problemController;

    @Autowired
    public AdminView(OtherUserController logoutController,
                     AdminController registerController,
                     ProblemController problemController) {
        this.logoutController = logoutController;
        this.registerController = registerController;
        this.problemController = problemController;
    }

    // --- USER MANAGEMENT ---

    @GetMapping("/user/all")
    public ResponseEntity<ViewResponse<List<User>>> getAllUsers(@RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.getAllUsers(admin));
    }

    @GetMapping("/user/all/{status}")
    public ResponseEntity<ViewResponse<List<User>>> getAllUsers(@PathVariable String status,
                                                                @RequestAttribute("user") User admin) {
        UserStatusCode statusCode = UserStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> registerController.getAllUsers(statusCode, admin));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ViewResponse<UserState>> getUserState(@PathVariable Long id,
                                                                @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.getLastUserState(id, admin));
    }

    @GetMapping("/user/{id}/{status}")
    public ResponseEntity<ViewResponse<User>> getUser(@PathVariable Long id,
                                                      @PathVariable String status,
                                                      @RequestAttribute("user") User admin) {
        UserStatusCode statusCode = UserStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> registerController.getUser(id, statusCode, admin));
    }

    @PutMapping("/user/{id}/ban")
    public ResponseEntity<ViewResponse<UserState>> banUser(@PathVariable Long id,
                                                           @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.BANNED, admin));
    }

    @PutMapping("/user/{id}/unban")
    public ResponseEntity<ViewResponse<UserState>> unbanUser(@PathVariable Long id,
                                                             @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.ACTIVE, admin));
    }

    @PutMapping("/user/{id}/deactivate")
    public ResponseEntity<ViewResponse<UserState>> deactivateUser(@PathVariable Long id,
                                                                  @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.INACTIVE, admin));
    }

    @PutMapping("/user/{id}/activate")
    public ResponseEntity<ViewResponse<UserState>> activateUser(@PathVariable Long id,
                                                                @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.ACTIVE, admin));
    }

    // --- PROBLEM MANAGEMENT (ex CustomerServiceView) ---

    @GetMapping("/problem/all")
    public ResponseEntity<ViewResponse<List<Problem>>> getAllProblems(@RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getAllProblems(user));
    }

    @GetMapping("/problem/all/{status}")
    public ResponseEntity<ViewResponse<List<Problem>>> getAllProblems(@PathVariable String status,
                                                                      @RequestAttribute("user") User user) {
        ProblemStatusCode statusCode = ProblemStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> problemController.getAllProblems(user, statusCode));
    }

    @GetMapping("/problem/{id}")
    public ResponseEntity<ViewResponse<ProblemState>> getProblemState(@PathVariable Long id,
                                                                      @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblemState(id, user));
    }

    @GetMapping("/problem/{id}/{status}")
    public ResponseEntity<ViewResponse<Problem>> getProblem(@PathVariable Long id,
                                                            @PathVariable ProblemStatusCode status,
                                                            @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.getProblem(id, status, user));
    }

    @PutMapping("/problem/{id}/solve")
    public ResponseEntity<ViewResponse<Problem>> solveProblem(@PathVariable Long id,
                                                              @RequestAttribute("user") User user) {
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.SOLVED, user));
    }

    @PutMapping("/problem/{id}/reject")
    public ResponseEntity<ViewResponse<Problem>> rejectProblem(@PathVariable Long id,
                                                               @RequestBody JsonNode body,
                                                               @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.REJECTED, reason, user));
    }

    @PutMapping("/problem/{id}/close")
    public ResponseEntity<ViewResponse<Problem>> closeProblem(@PathVariable Long id,
                                                              @RequestBody JsonNode body,
                                                              @RequestAttribute("user") User user) {
        String reason = body.get("reason").asText();
        return genericCall(() -> problemController.updateProblem(id, ProblemStatusCode.CLOSED, reason, user));
    }
}

