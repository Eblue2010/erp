package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.Address;
import io.eliteblue.erp.core.repository.AddressRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Component
public class AddressService implements CoreErpService<Address, Long> {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<Address> getAll() {
        return repository.findAll();
    }

    @Override
    public Address findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(Address address) {
        repository.delete(address);
    }

    @Override
    public void save(Address address) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(address.getId() == null) {
            address.setCreatedBy(currentUserName);
            address.setDateCreated(new Date());
            address.setLastEditedBy(currentUserName);
            address.setLastUpdate(new Date());
            address.setOperation(DataOperation.CREATED.name());
        }
        else {
            address.setLastEditedBy(currentUserName);
            address.setLastUpdate(new Date());
            address.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(address);
    }
}
