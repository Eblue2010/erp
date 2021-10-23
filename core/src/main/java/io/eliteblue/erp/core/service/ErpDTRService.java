package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpDTR;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.ErpDTRRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpDTRService extends CoreErpServiceImpl implements CoreErpService<ErpDTR, Long> {

    @Autowired
    private ErpDTRRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpDTR> getAll() {
        return repository.findAll();
    }

    public List<ErpDTR> getAllFilteredLocation() {
        List<OperationsArea> assignedLocations = CurrentUserUtil.getOperationsAreas();
        return repository.getAllFiltered(assignedLocations, new Date());
    }

    @Override
    public ErpDTR findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpDTR erpDTR) {
        repository.delete(erpDTR);
    }

    @Override
    public void save(ErpDTR erpDTR) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpDTR.getId() == null) {
            erpDTR.setCreatedBy(currentUserName);
            erpDTR.setDateCreated(new Date());
            erpDTR.setLastEditedBy(currentUserName);
            erpDTR.setLastUpdate(new Date());
            erpDTR.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpDTR.setLastEditedBy(currentUserName);
            erpDTR.setLastUpdate(new Date());
            erpDTR.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpDTR);
    }
}
