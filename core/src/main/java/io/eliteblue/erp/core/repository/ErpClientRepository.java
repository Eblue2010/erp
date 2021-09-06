package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpClientRepository extends JpaRepository<ErpClient, Long> {

    ErpClient findByName(String name);
}
