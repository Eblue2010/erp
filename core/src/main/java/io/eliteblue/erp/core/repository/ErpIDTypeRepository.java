package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpIDType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpIDTypeRepository  extends JpaRepository<ErpIDType, Long> {

    ErpIDType findByName(String name);
}
