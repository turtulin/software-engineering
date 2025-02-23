package it.unicam.cs.ids2425.problem.repository;

import it.unicam.cs.ids2425.problem.model.ProblemState;
import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemStateRepository extends JpaRepository<ProblemState, Long> {
    List<ProblemState> findAllByEntity_Id(Long problemId);

    List<ProblemState> findAllByStatusCode(BaseStatusCode baseStatusCode);
}
