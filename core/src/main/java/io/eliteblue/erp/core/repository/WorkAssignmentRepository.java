package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpWorkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkAssignmentRepository extends JpaRepository<ErpWorkAssignment, Long>{

    ErpWorkAssignment findByWorkAssignment(String workAssignment);
}
