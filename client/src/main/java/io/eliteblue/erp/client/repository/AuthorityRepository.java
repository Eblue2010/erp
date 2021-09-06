package io.eliteblue.erp.client.repository;

import io.eliteblue.erp.client.model.security.Authority;
import io.eliteblue.erp.client.model.security.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityName name);
}
