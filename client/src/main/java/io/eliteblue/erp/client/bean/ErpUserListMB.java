package io.eliteblue.erp.client.bean;

import io.eliteblue.erp.client.service.ErpUserService;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import io.eliteblue.erp.client.model.security.ErpUser;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ErpUserListMB implements Serializable {

    private List<ErpUser> users;

    private ErpUser selectedUser;

    @Autowired
    private ErpUserService erpUserService;

    @PostConstruct
    public void init() {
        users = erpUserService.getAllUsers();
    }

    public List<ErpUser> getUsers() {
        return users;
    }

    public void setErpUserService(ErpUserService erpUserService) {
        this.erpUserService = erpUserService;
    }

    public ErpUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(ErpUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void onRowSelect(SelectEvent<ErpUser> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user-form.xhtml?id="+event.getObject().getId());
    }

    public void onRowUnselect(UnselectEvent<ErpUser> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user-form.xhtml?id="+event.getObject().getId());
    }
}
