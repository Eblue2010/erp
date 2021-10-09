package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ErpEmployeeRepository extends JpaRepository<ErpEmployee, Long> {

    ErpEmployee findByFirstname(String firstname);
    ErpEmployee findByLastname(String lastname);

    @Query(value = "SELECT e FROM ErpEmployee e WHERE LOWER(e.status) != 'deleted' AND e.assignedLocation IN ?1")
    List<ErpEmployee> getAllFiltered(List<OperationsArea> areas);

    @Query(value = "SELECT e FROM ErpEmployee e WHERE LOWER(e.status) = 'hired'")
    List<ErpEmployee> getAllHired();

    @Query(value = "SELECT e FROM ErpEmployee e WHERE LOWER(e.status) != 'deleted'")
    List<ErpEmployee> getAllFilteredByDeleted();

    List<ErpEmployee> findByAssignedLocation(OperationsArea assignedLocation);

    List<ErpEmployee> findByAssignedLocationIn(List<OperationsArea> assignedLocations);

    List<ErpEmployee> findByFirstnameIgnoreCaseAndLastnameIgnoreCase(String firstname, String lastname);

    @Query(value = "SELECT last_value FROM employee_seq", nativeQuery = true)
    public BigDecimal getNextValSequence();

    @Query(value = "SELECT nextval(:sequenceName)", nativeQuery = true)
    public BigDecimal getNextSequenceByName(@Param("sequenceName") String sequenceName);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * FROM erp_employee LIMIT 1) THEN 1 ELSE 0 END", nativeQuery = true)
    public Integer getIsEmpty();
}
