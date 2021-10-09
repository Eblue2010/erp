package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ContactInfo;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.ErpEmployeeID;
import io.eliteblue.erp.core.model.ErpIDType;
import io.eliteblue.erp.core.service.*;
import io.eliteblue.erp.core.util.DateTimeUtil;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ContactInfoForm implements Serializable {

    private final String pattern = "dd MMM yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @Autowired
    private ErpEmployeeService employeeService;

    @Autowired
    private RegionCityService regionCityService;

    @Autowired
    private ContactInfoService contactInfoService;

    private Long id;
    private Long employeeId;
    private ErpEmployee erpEmployee;
    private String joinedDate;
    private String lastUpdate;
    private ContactInfo contactInfo;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            contactInfo = contactInfoService.findById(Long.valueOf(id));
            erpEmployee = contactInfo.getEmployee();
            employeeId = erpEmployee.getId();
            if(erpEmployee.getJoinedDate() != null) {
                joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
            }
            lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
        } else {
            contactInfo = new ContactInfo();
            if(has(employeeId)){
                erpEmployee = employeeService.findById(employeeId);
                contactInfo.setEmployee(erpEmployee);
                if(erpEmployee.getJoinedDate() != null) {
                    joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
                }
                lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public ErpEmployee getErpEmployee() {
        return erpEmployee;
    }

    public void setErpEmployee(ErpEmployee erpEmployee) {
        this.erpEmployee = erpEmployee;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String backBtnPressed() { return "employee-form?id="+employeeId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        contactInfo = new ContactInfo();
        id = null;
    }

    public boolean isNew() {
        return erpEmployee == null || contactInfo.getId() == null;
    }

    public void remove() throws Exception {
        if(has(contactInfo) && has(contactInfo.getId())) {
            String name = contactInfo.getContactNumber();
            contactInfoService.delete(contactInfo);
            addDetailMessage("CONTACT INFO DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void save() throws Exception {
        if(contactInfo != null) {
            contactInfoService.save(contactInfo);
            addDetailMessage("ID SAVED", contactInfo.getContactNumber(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
