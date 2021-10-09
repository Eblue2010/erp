package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpTimeSchedule;
import io.eliteblue.erp.core.repository.TimeScheduleRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TimeScheduleService extends CoreErpServiceImpl implements CoreErpService<ErpTimeSchedule, Long> {

    @Autowired
    private TimeScheduleRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpTimeSchedule> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpTimeSchedule findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpTimeSchedule erpTimeSchedule) {
        repository.delete(erpTimeSchedule);
    }

    @Override
    public void save(ErpTimeSchedule erpTimeSchedule) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpTimeSchedule.getId() == null) {
            erpTimeSchedule.setCreatedBy(currentUserName);
            erpTimeSchedule.setDateCreated(new Date());
            erpTimeSchedule.setLastEditedBy(currentUserName);
            erpTimeSchedule.setLastUpdate(new Date());
            erpTimeSchedule.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpTimeSchedule.setLastEditedBy(currentUserName);
            erpTimeSchedule.setLastUpdate(new Date());
            erpTimeSchedule.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpTimeSchedule);
    }
}
