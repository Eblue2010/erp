package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpWorkSchedule;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<ErpWorkSchedule, Long> {

    @Query(value = "SELECT e FROM ErpWorkSchedule e WHERE e.erpDetachment.location IN :operationAreas AND e.startDate > :operationStart")
    List<ErpWorkSchedule> getAllFiltered(@Param("operationAreas") List<OperationsArea> areas, @Param("operationStart") Date startDate);

    ErpWorkSchedule findByStartDateAndStopDate(Date startDate, Date stopDate);
}
