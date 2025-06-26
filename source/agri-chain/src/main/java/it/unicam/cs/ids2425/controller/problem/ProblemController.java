package it.unicam.cs.ids2425.controller.problem;

import it.unicam.cs.ids2425.model.problem.Problem;
import it.unicam.cs.ids2425.model.problem.ProblemState;
import it.unicam.cs.ids2425.repository.problem.ProblemRepository;
import it.unicam.cs.ids2425.repository.problem.ProblemStateRepository;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ProblemStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProblemController {
    private final ProblemRepository problemRepository;
    private final ProblemStateRepository problemStateRepository;

    @Autowired
    public ProblemController(ProblemRepository problemRepository, ProblemStateRepository ProblemStatusCodeRepository) {
        this.problemRepository = problemRepository;
        this.problemStateRepository = ProblemStatusCodeRepository;
    }

    @Transactional
    public Problem create(@NonNull Problem problem, @NonNull User user) {
        if (problem.getDescription() == null || problem.getDescription().isBlank()) {
            throw new IllegalArgumentException("Problem description is empty");
        }

        problem.setUser(user);
        ProblemState problemState = new ProblemState(ProblemStatusCode.OPEN, user, null, problem, null);
        problemRepository.save(problem);
        problemStateRepository.save(problemState);
        return problemRepository.findById(problem.getId()).orElseThrow(() -> new RuntimeException("Problem not found"));
    }

    private List<Problem> getAllProblems(ProblemStatusCode status) {
        if (status == null) {
            return problemRepository.findAll();
        }
        return problemRepository.findAll().stream()
                .map(p -> problemStateRepository.findAllByEntity_Id(p.getId()).stream().sorted().toList().getLast())
                .filter(ps -> ps.getStatusCode().equals(status))
                .map(ProblemState::getEntity).toList();
    }

    public List<Problem> getAllProblems(@NonNull User user) {
        return getAllProblems(user, null);
    }

    public List<Problem> getAllProblems(@NonNull User user, ProblemStatusCode status) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be an admin");
        }
        return getAllProblems(status);
    }

    public ProblemState getProblemState(@NonNull Long problemId, @NonNull User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be an admin");
        }
        return problemStateRepository
                .findAllByEntity_Id(problemId)
                .stream().sorted().toList().getLast();
    }

    public Problem getProblem(@NonNull Long problemId, @NonNull ProblemStatusCode statusCode, @NonNull User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be an admin");
        }
        ProblemState problemState = problemStateRepository
                .findAllByEntity_Id(problemId)
                .stream().sorted().toList().getLast();
        if (problemState.getStatusCode().equals(statusCode)) {
            return problemState.getEntity();
        }
        throw new NoSuchElementException("Problem not found");
    }

    public Problem updateProblem(@NonNull Long problemId, @NonNull ProblemStatusCode newStatus, @NonNull User user) {
        return updateProblem(problemId, newStatus, null, user);
    }

    public Problem updateProblem(@NonNull Long problemId, @NonNull ProblemStatusCode newStatus, String updateReason, @NonNull User user) {
        ProblemState oldState = getProblemState(problemId, user);
        // no new entity created because this is the problem status change, the entity data did not change.
        ProblemState problemState = new ProblemState(newStatus, user, updateReason, oldState.getEntity(), oldState);
        problemStateRepository.save(problemState);
        return problemRepository
                .findById(problemState.getEntity().getId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));
    }
}
