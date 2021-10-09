package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpWorkDay;
import io.eliteblue.erp.core.repository.WorkDayRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WorkDayService extends CoreErpServiceImpl implements CoreErpService<ErpWorkDay, Long> {

    @Autowired
    private WorkDayRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ErpWorkDay> getAll() {
        return repository.findAll();
    }

    @Override
    public ErpWorkDay findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ErpWorkDay erpWorkDay) {
        repository.delete(erpWorkDay);
    }

    @Override
    public void save(ErpWorkDay erpWorkDay) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(erpWorkDay.getId() == null) {
            erpWorkDay.setCreatedBy(currentUserName);
            erpWorkDay.setDateCreated(new Date());
            erpWorkDay.setLastEditedBy(currentUserName);
            erpWorkDay.setLastUpdate(new Date());
            erpWorkDay.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpWorkDay.setLastEditedBy(currentUserName);
            erpWorkDay.setLastUpdate(new Date());
            erpWorkDay.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(erpWorkDay);
    }
}
