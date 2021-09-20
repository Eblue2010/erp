package io.eliteblue.erp.core.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class CoreErpServiceImpl {

    private String currentUser;
    private Object principal;

    public CoreErpServiceImpl() {}

    public Object getPrincipal() {
        if(principal == null) {
            initializeService();
        }
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public String getCurrentUser() {
        if(currentUser == null) {
            initializeService();
        }
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    private void initializeService() {
        currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
