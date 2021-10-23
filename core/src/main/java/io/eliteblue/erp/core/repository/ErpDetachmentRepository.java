package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpDetachmentRepository extends JpaRepository<ErpDetachment, Long> {

    @Query(value = "SELECT e FROM ErpDetachment e WHERE e.location IN ?1")
    List<ErpDetachment> getAllFiltered(List<OperationsArea> areas);

    ErpDetachment findByName(String name);
}
