package io.eliteblue.erp.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.eliteblue.erp.client.model.security.ErpUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErpUserRepository extends JpaRepository<ErpUser, Long> {

    ErpUser findByUsername(String username);
}
