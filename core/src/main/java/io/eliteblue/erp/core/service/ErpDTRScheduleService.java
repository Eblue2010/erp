package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpDTRSchedule;
import io.eliteblue.erp.core.repository.ErpDTRScheduleRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpDTRScheduleService extends CoreErpServiceImpl implements CoreErpService<ErpDTRSchedule, Long> {

    @Autowired
    private ErpDTRScheduleRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpDTRSchedule> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpDTRSchedule findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpDTRSchedule erpDTRSchedule) {
        repository.delete(erpDTRSchedule);
    }

    @Override
    public void save(ErpDTRSchedule erpDTRSchedule) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpDTRSchedule.getId() == null) {
            erpDTRSchedule.setCreatedBy(currentUserName);
            erpDTRSchedule.setDateCreated(new Date());
            erpDTRSchedule.setLastEditedBy(currentUserName);
            erpDTRSchedule.setLastUpdate(new Date());
            erpDTRSchedule.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpDTRSchedule.setLastEditedBy(currentUserName);
            erpDTRSchedule.setLastUpdate(new Date());
            erpDTRSchedule.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpDTRSchedule);
    }
}
