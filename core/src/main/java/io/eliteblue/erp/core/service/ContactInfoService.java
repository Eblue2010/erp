package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ContactInfo;
import io.eliteblue.erp.core.repository.ContactInfoRepository;
import io.eliteblue.erp.core.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContactInfoService implements CoreErpService<ContactInfo, Long> {

    @Autowired
    private ContactInfoRepository repository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public List<ContactInfo> getAll() {
        return repository.findAll();
    }

    @Override
    public ContactInfo findById(Long aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public void delete(ContactInfo contactInfo) {
        repository.delete(contactInfo);
    }

    @Override
    public void save(ContactInfo contactInfo) {
        String currentUserName = CurrentUserUtil.getFullName();
        if(contactInfo.getId() == null) {
            contactInfo.setCreatedBy(currentUserName);
            contactInfo.setDateCreated(new Date());
            contactInfo.setLastEditedBy(currentUserName);
            contactInfo.setLastUpdate(new Date());
            contactInfo.setOperation(DataOperation.CREATED.name());
        }
        else {
            contactInfo.setLastEditedBy(currentUserName);
            contactInfo.setLastUpdate(new Date());
            contactInfo.setOperation(DataOperation.UPDATED.name());
        }
        repository.save(contactInfo);
    }
}
