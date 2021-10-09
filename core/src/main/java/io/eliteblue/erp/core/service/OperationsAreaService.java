package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.OperationsAreaRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OperationsAreaService extends CoreErpServiceImpl implements CoreErpService<OperationsArea, Long> {

    @Autowired
    private OperationsAreaRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    public List<OperationsArea> getAll() {
        return repository.findAll();
    }

    public OperationsArea findById(Long id) {
        return repository.getOne(id);
    }

    public OperationsArea findByLocation(String location) {
        return repository.findByLocation(location);
    }

    public void delete(OperationsArea operationsArea) {
        repository.delete(operationsArea);
    }

    public void save(OperationsArea operationsArea) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(operationsArea.getId() == null) {
            operationsArea.setCreatedBy(currentUserName);
            operationsArea.setLastEditedBy(currentUserName);
            operationsArea.setDateCreated(new Date());
            operationsArea.setOperation(DataOperation.CREATED.toString());
            operationsArea.setCreatedBy(super.getCurrentUser());
        }
        else {
            operationsArea.setOperation(DataOperation.UPDATED.toString());
            operationsArea.setLastUpdate(new Date());
            operationsArea.setLastEditedBy(currentUserName);
        }
        operationsArea.setLastUpdate(new Date());
        repository.save(operationsArea);
    }
}
