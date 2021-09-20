package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.security.Authority;
import io.eliteblue.erp.core.model.security.AuthorityName;
import io.eliteblue.erp.core.model.security.ErpUser;
import io.eliteblue.erp.core.service.ErpUserService;
import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpUserFormMB implements Serializable {

    @Autowired
    private ErpUserService erpUserService;

    @Autowired
    private OperationsAreaService operationsAreaService;

    private Long id;
    private ErpUser user;
    private Map<String, Boolean> enabledValues;
    private List<String> userRoles;
    private List<String> selectedRoles;
    private List<String> locations;
    private List<String> selectedLocations;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            user = erpUserService.findById(Long.valueOf(id));
        } else {
            user = new ErpUser();
        }
        enabledValues = new HashMap<>();
        enabledValues.put("YES", true);
        enabledValues.put("NO", false);

        List<OperationsArea> areas = operationsAreaService.getAll();
        locations = new ArrayList<>();
        for(OperationsArea o: areas) {
            locations.add(o.getLocation());
        }

        userRoles = new ArrayList<>();
        for(AuthorityName a : AuthorityName.values()) {
            userRoles.add(a.name().replaceAll("ROLE_", ""));
        }
        if(user != null && user.getId() != null) {
            selectedRoles = new ArrayList<>();
            selectedLocations = new ArrayList<>();
            for(Authority a : user.getAuthorities()) {
                selectedRoles.add(a.getName().name().replaceAll("ROLE_", ""));
            }
            for(OperationsArea ar: user.getLocations()) {
                selectedLocations.add(ar.getLocation());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpUser getUser() {
        return user;
    }

    public void setUser(ErpUser user) {
        this.user = user;
    }

    public Map<String, Boolean> getEnabledValues() {
        return enabledValues;
    }

    public void setEnabledValues(Map<String, Boolean> enabledValues) {
        this.enabledValues = enabledValues;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<String> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<String> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public void clear() {
        user = new ErpUser();
        id = null;
    }

    public boolean isNew() {
        return user == null || user.getId() == null;
    }

    public void remove() throws Exception {
        if(has(user) && has(user.getId())) {
            String userFullName = user.getFirstname()+ " " +user.getLastname();
            erpUserService.delete(user);
            addDetailMessage("USER DELETED", userFullName+".", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("users-management.xhtml");
        }
    }

    public void save() throws Exception {
        if(user != null) {
            if(selectedRoles != null && selectedRoles.size() > 0) {
                user.setAuthorities(new HashSet<>());
                for(String r: selectedRoles) {
                    Authority au = erpUserService.findAuthorityByName(AuthorityName.valueOf("ROLE_"+r));
                    if(au == null) {
                        au = new Authority();
                        au.setName(AuthorityName.valueOf("ROLE_"+r));
                    }
                    user.getAuthorities().add(au);
                }
            }
            if(selectedLocations != null && selectedLocations.size() > 0) {
                user.setLocations(new HashSet<>());
                for(String l: selectedLocations) {
                    OperationsArea area = new OperationsArea();
                    area = erpUserService.findAreaByLocation(l);
                    if(area == null) {
                        area.setOperation(DataOperation.CREATED.name());
                        area.setLastUpdate(new Date());
                        area.setLocation(l);
                    }
                    user.getLocations().add(area);
                }
            }
            erpUserService.save(user);
            String userFullName = user.getFirstname()+ " " +user.getLastname();
            addDetailMessage("USER SAVED", userFullName+".", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("users-management.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
