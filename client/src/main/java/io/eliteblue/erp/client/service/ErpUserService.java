package io.eliteblue.erp.client.service;

import io.eliteblue.erp.client.model.security.*;
import io.eliteblue.erp.client.repository.AuthorityRepository;
import io.eliteblue.erp.client.repository.ErpUserRepository;
import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.OperationsAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

@Component
public class ErpUserService implements Serializable {

    private final static String USERNAME_TAKEN = "user with username %s is taken";

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ErpUserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private OperationsAreaRepository operationsAreaRepository;

    public List<ErpUser> getAllUsers() {
        return repository.findAll();
    }

    public List<OperationsArea> getAllAreas() { return operationsAreaRepository.findAll(); }

    public OperationsArea findAreaByLocation(String location) {
        return operationsAreaRepository.findByLocation(location);
    }

    public ErpUser findById(Long id) {
        return repository.getOne(id);
    }

    public void delete(ErpUser t) {
        repository.delete(t);
    }

    public Authority findAuthorityByName(AuthorityName name) {
        return authorityRepository.findByName(name);
    }

    public Long save(ErpUser user) throws Exception {
        if(user != null) {
            Set<Authority> authorityList = new HashSet<>();
            Set<OperationsArea> areaList = new HashSet<>();
            if(user.getId() == null) {
                ErpUser dbUser = repository.findByUsername(user.getUsername());
                boolean userExists = dbUser != null;
                String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

                if(userExists) {
                    throw new IllegalStateException(String.format(USERNAME_TAKEN, user.getUsername()));
                }

                user.setLastPasswordResetDate(new Date());
                user.setPassword(encodedPassword);
                user.setOperation(DataOperation.CREATED.name());
                user.setDateCreated(new Date());
                user.setLastUpdate(new Date());
                if(user.getEnabled() == null) {
                    user.setEnabled(true);
                }
            }
            else {
                user.setOperation(DataOperation.UPDATED.name());
                user.setLastUpdate(new Date());
            }
            if(user.getAuthorities() != null) {
                for(Authority a: user.getAuthorities()) {
                    if(a.getId() == null) {
                        a = saveAuthorityName(a.getName());
                    }
                    authorityList.add(a);
                }
                user.setAuthorities(authorityList);
            }
            else {
                Authority authority = authorityRepository.findByName(AuthorityName.ROLE_USER);
                if(authority == null) {
                    authority = saveAuthorityName(AuthorityName.ROLE_USER);
                }
                authorityList.add(authority);
                user.setAuthorities(authorityList);
            }
            if(user.getLocations() != null) {
                for(OperationsArea l: user.getLocations()) {
                    if(l.getId() == null) {
                        l = saveUserArea(l.getLocation());
                    }
                    areaList.add(l);
                }
            }
            else {
                OperationsArea area = operationsAreaRepository.findByLocation("FIELD OFFICE");
                if(area == null) {
                    area = saveUserArea("FIELD OFFICE");
                }
                areaList.add(area);
                user.setLocations(areaList);
            }
            user.setLastUpdate(new Date());
            return repository.save(user).getId();
        }
        return null;
    }

    public Authority saveAuthorityName(AuthorityName name) {
        Authority retVal = new Authority();
        retVal.setName(name);
        retVal.setDateCreated(new Date());
        retVal.setLastUpdate(new Date());
        retVal.setOperation(DataOperation.CREATED.name());
        return authorityRepository.save(retVal);
    }

    public OperationsArea saveUserArea(String location) {
        OperationsArea retVal = new OperationsArea();
        retVal.setLocation(location);
        retVal.setLastUpdate(new Date());
        retVal.setDateCreated(new Date());
        retVal.setOperation(DataOperation.CREATED.name());
        return operationsAreaRepository.save(retVal);
    }

    public ErpUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public ErpUserDetails getUserDetails(String username) {
        ErpUser user = repository.findByUsername(username);

        if(user == null)
            return null;

        return new ErpUserDetails(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>(user.getAuthorities()),
                new Date(),
                false,
                user.getEnabled()
        );
    }

    public ErpUser getErpUser(String username) {
        System.out.println("getErpUser() METHOD: "+username);
        ErpUser user = repository.findByUsername(username);
        return user;
    }
}
