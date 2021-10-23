package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.ErpDetachmentRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpDetachmentService extends CoreErpServiceImpl implements CoreErpService<ErpDetachment, Long> {

    @Autowired
    private ErpDetachmentRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    public List<ErpDetachment> getAll() {
        return repository.findAll();
    }

    public List<ErpDetachment> getAllFilteredLocation() {
        List<OperationsArea> assignedLocations = CurrentUserUtil.getOperationsAreas();
        return repository.getAllFiltered(assignedLocations);
    }

    public ErpDetachment findById(Long aLong) {
        return repository.getOne(aLong);
    }

    public void delete(ErpDetachment erpDetachment) {
        repository.delete(erpDetachment);
    }

    public void save(ErpDetachment erpDetachment) {
        if(erpDetachment.getId() == null) {
            erpDetachment.setDateCreated(new Date());
            erpDetachment.setOperation(DataOperation.CREATED.name());
            erpDetachment.setCreatedBy(super.getCurrentUser());
        }
        else {
            erpDetachment.setOperation(DataOperation.UPDATED.name());
            erpDetachment.setLastUpdate(new Date());
            erpDetachment.setLastEditedBy(super.getCurrentUser());
        }
        erpDetachment.setLastUpdate(new Date());
        repository.save(erpDetachment);
    }
}
