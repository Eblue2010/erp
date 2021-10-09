package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WorkScheduleRepository extends JpaRepository<ErpWorkSchedule, Long> {

    ErpWorkSchedule findByStartDateAndStopDate(Date startDate, Date stopDate);
}
