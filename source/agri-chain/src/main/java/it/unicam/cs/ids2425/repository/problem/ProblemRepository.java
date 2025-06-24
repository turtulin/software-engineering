package it.unicam.cs.ids2425.repository.problem;

import it.unicam.cs.ids2425.model.problem.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
