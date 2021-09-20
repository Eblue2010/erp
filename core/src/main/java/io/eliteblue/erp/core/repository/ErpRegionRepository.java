package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpRegionRepository extends JpaRepository<ErpRegion, Long> {

    ErpRegion findByName(String name);
}
