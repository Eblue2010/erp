package io.eliteblue.erp.client.service;

import io.eliteblue.erp.client.model.security.Authority;
import io.eliteblue.erp.client.repository.AuthorityRepository;
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
