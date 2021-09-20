package io.eliteblue.erp.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.eliteblue.erp.core.model.security.ErpUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpUserRepository extends JpaRepository<ErpUser, Long> {

    ErpUser findByUsername(String username);
}
