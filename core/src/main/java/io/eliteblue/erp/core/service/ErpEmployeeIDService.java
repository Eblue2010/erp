package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpEmployeeID;
import io.eliteblue.erp.core.repository.ErpEmployeeIDRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpEmployeeIDService implements CoreErpService<ErpEmployeeID, Long> {

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private ErpEmployeeIDRepository repository;

    @Override
    public List<ErpEmployeeID> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpEmployeeID findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpEmployeeID erpEmployeeID) {
        repository.delete(erpEmployeeID);
    }

    @Override
    public void save(ErpEmployeeID erpEmployeeID) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpEmployeeID.getId() == null) {
            erpEmployeeID.setCreatedBy(currentUserName);
            erpEmployeeID.setDateCreated(new Date());
            erpEmployeeID.setLastEditedBy(currentUserName);
            erpEmployeeID.setLastUpdate(new Date());
            erpEmployeeID.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpEmployeeID.setLastEditedBy(currentUserName);
            erpEmployeeID.setLastUpdate(new Date());
            erpEmployeeID.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpEmployeeID);
    }
}
