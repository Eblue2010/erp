package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.model.security.Authority;
import io.eliteblue.erp.core.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AuthorityService implements Serializable {

    @Autowired
    private AuthorityRepository repository;

    public Long save(Authority authority) {
        return repository.save(authority).getId();
    }
}
