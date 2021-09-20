package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ErpEmployeeRepository extends JpaRepository<ErpEmployee, Long> {

    ErpEmployee findByFirstname(String firstname);
    ErpEmployee findByLastname(String lastname);

    List<ErpEmployee> findByAssignedLocation(OperationsArea assignedLocation);

    List<ErpEmployee> findByAssignedLocationIn(List<OperationsArea> assignedLocations);

    @Query(value = "SELECT last_value FROM employee_seq", nativeQuery = true)
    public BigDecimal getNextValSequence();

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * FROM erp_employee LIMIT 1) THEN 1 ELSE 0 END", nativeQuery = true)
    public Integer getIsEmpty();
}
