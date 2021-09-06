package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationsAreaRepository extends JpaRepository<OperationsArea, Long> {

    OperationsArea findByLocation(String location);
}
