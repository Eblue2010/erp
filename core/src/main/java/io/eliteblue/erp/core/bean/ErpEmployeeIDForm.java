package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.*;
import io.eliteblue.erp.core.service.ErpEmployeeIDService;
import io.eliteblue.erp.core.service.ErpEmployeeService;
import io.eliteblue.erp.core.service.ErpIDTypeService;
import io.eliteblue.erp.core.service.RegionCityService;
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
public class ErpEmployeeIDForm implements Serializable {

    private final String pattern = "dd MMM yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @Autowired
    private ErpEmployeeIDService erpEmployeeIDService;

    @Autowired
    private ErpEmployeeService employeeService;

    @Autowired
    private RegionCityService regionCityService;

    @Autowired
    private ErpIDTypeService idTypeService;

    private Long id;
    private Long employeeId;
    private ErpEmployee erpEmployee;
    private ErpEmployeeID erpEmployeeID;
    private String joinedDate;
    private String lastUpdate;
    private Map<String, Long> idTypes;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpEmployeeID = erpEmployeeIDService.findById(Long.valueOf(id));
            erpEmployee = erpEmployeeID.getEmployee();
            employeeId = erpEmployee.getId();
            if(erpEmployee.getJoinedDate() != null) {
                joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
            }
            lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
        } else {
            erpEmployeeID = new ErpEmployeeID();
            erpEmployeeID.setIdName(new ErpIDType());
            if(has(employeeId)){
                erpEmployee = employeeService.findById(employeeId);
                erpEmployeeID.setEmployee(erpEmployee);
                if(erpEmployee.getJoinedDate() != null) {
                    joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
                }
                lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
            }
        }
        idTypes = new HashMap<>();
        List<ErpIDType> idTypeList = idTypeService.getAll();
        for(ErpIDType i: idTypeList) {
            idTypes.put(i.getName(), i.getId());
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

    public ErpEmployeeID getErpEmployeeID() {
        return erpEmployeeID;
    }

    public void setErpEmployeeID(ErpEmployeeID erpEmployeeID) {
        this.erpEmployeeID = erpEmployeeID;
    }

    public Map<String, Long> getIdTypes() {
        return idTypes;
    }

    public void setIdTypes(Map<String, Long> idTypes) {
        this.idTypes = idTypes;
    }

    public String backBtnPressed() { return "employee-form?id="+employeeId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpEmployeeID = new ErpEmployeeID();
        id = null;
    }

    public boolean isNew() {
        return erpEmployee == null || erpEmployeeID.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpEmployeeID) && has(erpEmployeeID.getId())) {
            String name = erpEmployeeID.getIdName().getName();
            erpEmployeeIDService.delete(erpEmployeeID);
            addDetailMessage("EMPLOYEE ID DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void save() throws Exception {
        if(erpEmployeeID != null && erpEmployeeID.getIdName().getId() != null) {
            ErpIDType type = idTypeService.findById(erpEmployeeID.getIdName().getId());
            erpEmployeeID.setIdName(type);
            erpEmployeeIDService.save(erpEmployeeID);
            addDetailMessage("ID SAVED", erpEmployeeID.getIdName().getName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
