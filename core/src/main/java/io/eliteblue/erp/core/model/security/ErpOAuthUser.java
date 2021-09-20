package io.eliteblue.erp.core.model.security;

import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class ErpOAuthUser implements OAuth2User {

    private OAuth2User oAuth2User;

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Date lastLogged;
    private Boolean locked;
    private Boolean enabled;
    private String clientname;
    private List<OperationsArea> operationsAreas;

    public ErpOAuthUser(OAuth2User oauth2User, String clientName) {
        this.oAuth2User = oauth2User;
        this.clientname = clientName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public OAuth2User getoAuth2User() {
        return oAuth2User;
    }

    public void setoAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public String getFullName() {
        return oAuth2User.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Date getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(Date lastLogged) {
        this.lastLogged = lastLogged;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public List<OperationsArea> getOperationsAreas() {
        return operationsAreas;
    }

    public void setOperationsAreas(List<OperationsArea> operationsAreas) {
        this.operationsAreas = operationsAreas;
    }
}
