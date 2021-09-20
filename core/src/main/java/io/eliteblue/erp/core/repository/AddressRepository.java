package io.eliteblue.erp.core.repository;

import io.eliteblue.erp.core.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByAddressName(String name);
}
