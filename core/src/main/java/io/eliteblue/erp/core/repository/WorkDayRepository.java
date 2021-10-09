package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpWorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDayRepository extends JpaRepository<ErpWorkDay, Long> {

}
