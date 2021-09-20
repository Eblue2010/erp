package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    ContactInfo findByContactNumber(String contactNumber);
}
