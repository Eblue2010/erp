package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpEmployeeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpEmployeeIDRepository extends JpaRepository<ErpEmployeeID, Long> {

    ErpEmployeeID findByIdentificationNumber(String idNumber);
}
