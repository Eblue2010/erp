package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ErpPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpPostRepository extends JpaRepository<ErpPost, Long> {

    ErpPost findByName(String name);
}
