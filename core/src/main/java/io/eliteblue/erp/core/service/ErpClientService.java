package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.repository.ErpClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpClientService implements CoreErpService<ErpClient, Long> {

    @Autowired
    private ErpClientRepository repository;

    public List<ErpClient> getAll() {
        return repository.findAll();
    }

    public ErpClient findById(Long aLong) {
        return repository.getOne(aLong);
    }

    public void delete(ErpClient erpClient) {
        repository.delete(erpClient);
    }

    public void save(ErpClient erpClient) {
        if(erpClient.getId() == null) {
            erpClient.setDateCreated(new Date());
            erpClient.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpClient.setOperation(DataOperation.UPDATED.name());
        }
        erpClient.setLastUpdate(new Date());
        repository.save(erpClient);
    }
}
