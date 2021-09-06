package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.repository.ErpPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ErpPostService implements CoreErpService<ErpPost, Long> {

    @Autowired
    private ErpPostRepository repository;

    public List<ErpPost> getAll() {
        return repository.findAll();
    }

    public ErpPost findById(Long aLong) {
        return repository.getOne(aLong);
    }

    public void delete(ErpPost erpPost) {
        repository.delete(erpPost);
    }

    public void save(ErpPost erpPost) {
        if(erpPost.getId() == null) {
            erpPost.setDateCreated(new Date());
            erpPost.setOperation(DataOperation.CREATED.name());
        }
        else {
            erpPost.setOperation(DataOperation.UPDATED.name());
        }
        erpPost.setLastUpdate(new Date());
        repository.save(erpPost);
    }
}
