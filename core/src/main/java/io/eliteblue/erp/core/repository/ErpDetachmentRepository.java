package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpDetachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpDetachmentRepository extends JpaRepository<ErpDetachment, Long> {

    ErpDetachment findByName(String name);
}
