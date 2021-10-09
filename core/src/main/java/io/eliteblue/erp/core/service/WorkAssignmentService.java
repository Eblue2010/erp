package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpWorkAssignment;
import io.eliteblue.erp.core.repository.WorkAssignmentRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WorkAssignmentService extends CoreErpServiceImpl implements CoreErpService<ErpWorkAssignment, Long> {

    @Autowired
    private WorkAssignmentRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpWorkAssignment> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpWorkAssignment findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpWorkAssignment erpWorkAssignment) {
        repository.delete(erpWorkAssignment);
    }

    @Override
    public void save(ErpWorkAssignment erpWorkAssignment) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpWorkAssignment.getId() == null) {
            erpWorkAssignment.setCreatedBy(currentUserName);
            erpWorkAssignment.setDateCreated(new Date());
            erpWorkAssignment.setLastEditedBy(currentUserName);
            erpWorkAssignment.setLastUpdate(new Date());
            erpWorkAssignment.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpWorkAssignment.setLastEditedBy(currentUserName);
            erpWorkAssignment.setLastUpdate(new Date());
            erpWorkAssignment.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpWorkAssignment);
    }
}
