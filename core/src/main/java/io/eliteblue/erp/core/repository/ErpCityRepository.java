package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpCity;
import io.eliteblue.erp.core.model.ErpRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpCityRepository extends JpaRepository<ErpCity, Long> {

    ErpCity findByName(String name);
    List<ErpCity> findAllByErpRegion(ErpRegion erpRegion);
}
