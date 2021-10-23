package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpDTRSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpDTRScheduleRepository extends JpaRepository<ErpDTRSchedule, Long> {

}
