package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpDTRAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpDTRAssignmentRepository extends JpaRepository<ErpDTRAssignment, Long> {

}
