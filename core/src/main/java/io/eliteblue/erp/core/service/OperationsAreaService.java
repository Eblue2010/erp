package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.OperationsAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OperationsAreaService implements CoreErpService<OperationsArea, Long> {

    @Autowired
    private OperationsAreaRepository repository;

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
        if(operationsArea.getId() == null) {
            operationsArea.setDateCreated(new Date());
            operationsArea.setOperation(DataOperation.CREATED.toString());
        }
        else {
            operationsArea.setOperation(DataOperation.UPDATED.toString());
        }
        operationsArea.setLastUpdate(new Date());
        repository.save(operationsArea);
    }
}
