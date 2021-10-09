package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpWorkSchedule;
import io.eliteblue.erp.core.repository.WorkScheduleRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WorkScheduleService extends CoreErpServiceImpl implements CoreErpService<ErpWorkSchedule, Long> {

    @Autowired
    private WorkScheduleRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpWorkSchedule> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpWorkSchedule findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpWorkSchedule erpWorkSchedule) {
        repository.delete(erpWorkSchedule);
    }

    @Override
    public void save(ErpWorkSchedule erpWorkSchedule) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpWorkSchedule.getId() == null) {
            erpWorkSchedule.setCreatedBy(currentUserName);
            erpWorkSchedule.setDateCreated(new Date());
            erpWorkSchedule.setLastEditedBy(currentUserName);
            erpWorkSchedule.setLastUpdate(new Date());
            erpWorkSchedule.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpWorkSchedule.setLastEditedBy(currentUserName);
            erpWorkSchedule.setLastUpdate(new Date());
            erpWorkSchedule.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpWorkSchedule);
    }
}
