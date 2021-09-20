package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.bean.SessionMB;
import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.ErpEmployeeRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Component
public class ErpEmployeeService extends CoreErpServiceImpl implements CoreErpService<ErpEmployee, Long> {

    @Autowired
    private ErpEmployeeRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    private final DecimalFormat decimalFormat = new DecimalFormat("00000000");
    private final DecimalFormat areaFormat = new DecimalFormat("00");

    @Override
    public List<ErpEmployee> getAll() {
        return repository.findAll();
    }

    public List<ErpEmployee> getAllByAssignedLocation() {
        OperationsArea assignedLocation = null;
        return repository.findByAssignedLocation(assignedLocation);
    }

    @Override
    public ErpEmployee findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpEmployee erpEmployee) {
        String currentUserName = CurrentUserUtil.getFullName();
        erpEmployee.setOperation(DataOperation.REMOVED.name());
        erpEmployee.setLastEditedBy(currentUserName);
        erpEmployee.setLastUpdate(new Date());
        repository.save(erpEmployee);
    }

    @Override
    public void save(ErpEmployee erpEmployee) {
        String currentUserName = CurrentUserUtil.getFullName();
        List<OperationsArea> areas = CurrentUserUtil.getOperationsAreas();
        String operationAreaCode = "99";
        for(OperationsArea o: areas) {
            operationAreaCode = areaFormat.format(o.getId());
        }
        if(erpEmployee.getId() == null) {
            erpEmployee.setOperation(DataOperation.CREATED.name());
            erpEmployee.setLastEditedBy(currentUserName);
            erpEmployee.setCreatedBy(currentUserName);
            erpEmployee.setLastUpdate(new Date());
            erpEmployee.setDateCreated(new Date());
            BigDecimal seq = repository.getNextValSequence();
            Long seqNum = seq.longValue();
            if(seq.intValue() <= 1) {
                Integer isEmpty = repository.getIsEmpty();
                if(isEmpty != 0){
                    seqNum += 1;
                }
            }
            else {
                seqNum += 1;
            }
            erpEmployee.setEmployeeId(operationAreaCode + decimalFormat.format(seqNum));
        }
        else {
            erpEmployee.setOperation(DataOperation.UPDATED.name());
            erpEmployee.setLastEditedBy(currentUserName);
            erpEmployee.setLastUpdate(new Date());
        }
        repository.save(erpEmployee);
    }

    public void saveAll(List<ErpEmployee> employees) {
        repository.saveAll(employees);
    }
}
