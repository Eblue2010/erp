package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpDTRAssignment;
import io.eliteblue.erp.core.repository.ErpDTRAssignmentRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpDTRAssignmentService extends CoreErpServiceImpl implements CoreErpService<ErpDTRAssignment, Long> {

    @Autowired
    private ErpDTRAssignmentRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpDTRAssignment> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpDTRAssignment findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpDTRAssignment erpDTRAssignment) {
        repository.delete(erpDTRAssignment);
    }

    @Override
    public void save(ErpDTRAssignment erpDTRAssignment) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpDTRAssignment.getId() == null) {
            erpDTRAssignment.setCreatedBy(currentUserName);
            erpDTRAssignment.setDateCreated(new Date());
            erpDTRAssignment.setLastEditedBy(currentUserName);
            erpDTRAssignment.setLastUpdate(new Date());
            erpDTRAssignment.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpDTRAssignment.setLastEditedBy(currentUserName);
            erpDTRAssignment.setLastUpdate(new Date());
            erpDTRAssignment.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpDTRAssignment);
    }
}
