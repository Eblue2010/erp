package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpDTR;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ErpDTRRepository extends JpaRepository<ErpDTR, Long> {

    @Query(value = "SELECT e FROM ErpDTR e WHERE e.erpDetachment.location IN :operationAreas AND e.cutoffStart > :operationStart")
    List<ErpDTR> getAllFiltered(@Param("operationAreas") List<OperationsArea> areas, @Param("operationStart") Date startDate);
}
