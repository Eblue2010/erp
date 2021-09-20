package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyErpUserModel;
import io.eliteblue.erp.core.service.ErpUserService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import io.eliteblue.erp.core.model.security.ErpUser;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@Named
@ViewScoped
public class ErpUserListMB implements Serializable {

    private LazyDataModel<ErpUser> lazyErpUsers;

    private List<ErpUser> filteredErpUsers;

    private List<ErpUser> users;

    private ErpUser selectedUser;

    @Autowired
    private ErpUserService erpUserService;

    @PostConstruct
    public void init() {
        users = erpUserService.getAllUsers();
        lazyErpUsers = new LazyErpUserModel(users);
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

    public LazyDataModel<ErpUser> getLazyErpUsers() {
        return lazyErpUsers;
    }

    public void setLazyErpUsers(LazyDataModel<ErpUser> lazyErpUsers) {
        this.lazyErpUsers = lazyErpUsers;
    }

    public List<ErpUser> getFilteredErpUsers() {
        return filteredErpUsers;
    }

    public void setFilteredErpUsers(List<ErpUser> filteredErpUsers) {
        this.filteredErpUsers = filteredErpUsers;
    }

    public void onRowSelect(SelectEvent<ErpUser> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user-form.xhtml?id="+selectedUser.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpUser> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user-form.xhtml?id="+selectedUser.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);
        ErpUser erpUser = (ErpUser) value;
        return erpUser.getUsername().toLowerCase().contains(filterText)
                || erpUser.getFirstname().toLowerCase().contains(filterText)
                || erpUser.getLastname().toLowerCase().contains(filterText)
                || erpUser.getEmail().toString().toLowerCase().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
}
