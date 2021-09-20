package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.constants.BloodType;
import io.eliteblue.erp.core.constants.CivilStatus;
import io.eliteblue.erp.core.constants.EmployeeStatus;
import io.eliteblue.erp.core.constants.Gender;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.service.ErpEmployeeService;
import io.eliteblue.erp.core.util.DateTimeUtil;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpEmployeeForm implements Serializable {

    @Autowired
    private ErpEmployeeService employeeService;

    private final String pattern = "dd MMM yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    private Long id;
    private ErpEmployee erpEmployee;
    private Map<String, Gender> genderValues;
    private Map<String, CivilStatus> maritalValues;
    private Map<String, EmployeeStatus> empStatusValues;
    private Map<String, BloodType> bloodValues;
    private EmployeeStatus initialStatus;
    private String joinedDate;
    private String lastUpdate;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpEmployee = employeeService.findById(Long.valueOf(id));
            initialStatus = erpEmployee.getStatus();
            if(erpEmployee.getJoinedDate() != null) {
                joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
            }
            lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
        } else {
            erpEmployee = new ErpEmployee();
            initialStatus = EmployeeStatus.CREATED;
            joinedDate = "Joined 09 Dec 2017";
        }
        genderValues = new HashMap<>();
        for(Gender g: Gender.values()) {
            genderValues.put(g.name(), g);
        }
        maritalValues = new HashMap<>();
        for(CivilStatus c: CivilStatus.values()) {
            maritalValues.put(c.name(), c);
        }
        empStatusValues = new HashMap<>();
        for(EmployeeStatus e: EmployeeStatus.values()) {
            empStatusValues.put(e.name(), e);
        }
        bloodValues = new HashMap<>();
        for(BloodType b: BloodType.values()) {
            bloodValues.put(b.toString(), b);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ErpEmployee getErpEmployee() {
        return erpEmployee;
    }

    public Map<String, Gender> getGenderValues() {
        return genderValues;
    }

    public void setGenderValues(Map<String, Gender> genderValues) {
        this.genderValues = genderValues;
    }

    public Map<String, CivilStatus> getMaritalValues() {
        return maritalValues;
    }

    public void setMaritalValues(Map<String, CivilStatus> maritalValues) {
        this.maritalValues = maritalValues;
    }

    public Map<String, EmployeeStatus> getEmpStatusValues() {
        return empStatusValues;
    }

    public void setEmpStatusValues(Map<String, EmployeeStatus> empStatusValues) {
        this.empStatusValues = empStatusValues;
    }

    public Map<String, BloodType> getBloodValues() {
        return bloodValues;
    }

    public void setBloodValues(Map<String, BloodType> bloodValues) {
        this.bloodValues = bloodValues;
    }

    public void setErpEmployee(ErpEmployee erpEmployee) {
        this.erpEmployee = erpEmployee;
    }

    public String newAddressPressed() {
        return "address-form?employeeId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public String editAddressPressed(Long addressId) {
        return "address-form?employeeId="+id+"&id="+addressId+"&faces-redirect=true&includeViewParams=true";
    }

    public boolean isNew() {
        return erpEmployee == null || erpEmployee.getId() == null;
    }

    public void clear() {
        erpEmployee = new ErpEmployee();
        id = null;
    }

    public void remove() throws Exception {
        if(has(erpEmployee) && has(erpEmployee.getId())) {
            String userFullName = erpEmployee.getFirstname()+ " " +erpEmployee.getLastname();
            employeeService.delete(erpEmployee);
            addDetailMessage("EMPLOYEE DELETED", userFullName+".", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employees.xhtml");
        }
    }

    public void save() throws Exception {
        if(erpEmployee != null) {
            if(erpEmployee.getWeightPound() != null) {
                erpEmployee.setWeightKilo(erpEmployee.getWeightPound() * 0.454);
            }
            if(!initialStatus.equals(erpEmployee.getStatus())) {
                if(erpEmployee.getStatus().equals(EmployeeStatus.HIRED) && erpEmployee.getJoinedDate() == null) {
                    erpEmployee.setJoinedDate(new Date());
                }
            }
            employeeService.save(erpEmployee);
            String userFullName = erpEmployee.getFirstname()+ " " +erpEmployee.getLastname();
            addDetailMessage("EMPLOYEE SAVED", userFullName+".", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employees.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
