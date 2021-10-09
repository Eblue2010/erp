package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpIDType;
import io.eliteblue.erp.core.repository.ErpIDTypeRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpIDTypeService extends CoreErpServiceImpl implements CoreErpService<ErpIDType, Long> {

    @Autowired
    private ErpIDTypeRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpIDType> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpIDType findById(Long aLong) {
        return repository.getOne(aLong);
    }

    public ErpIDType findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void delete(ErpIDType erpIDType) {
        repository.delete(erpIDType);
    }

    @Override
    public void save(ErpIDType erpIDType) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpIDType.getId() == null) {
            erpIDType.setCreatedBy(currentUserName);
            erpIDType.setLastEditedBy(currentUserName);
            erpIDType.setDateCreated(new Date());
            erpIDType.setOperation(DataOperation.CREATED.toString());
            erpIDType.setCreatedBy(super.getCurrentUser());
        }
        else {
            erpIDType.setOperation(DataOperation.UPDATED.toString());
            erpIDType.setLastUpdate(new Date());
            erpIDType.setLastEditedBy(currentUserName);
        }
        erpIDType.setLastUpdate(new Date());
        repository.save(erpIDType);
    }
}
