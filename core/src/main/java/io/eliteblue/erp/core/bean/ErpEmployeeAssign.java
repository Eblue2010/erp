package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.ErpEmployeeService;
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
public class ErpEmployeeAssign implements Serializable {

    @Autowired
    private ErpEmployeeService erpEmployeeService;

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private Long detachmentId;
    private ErpDetachment detachment;
    private List<ErpEmployee> employees;
    private List<ErpEmployee> selectedEmployees;
    private Map<String, ErpEmployee> assignedEmployees;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(detachmentId)) {
            detachment = erpDetachmentService.findById(Long.valueOf(detachmentId));
            employees = new ArrayList<>(detachment.getAssignedEmployees());
            selectedEmployees = new ArrayList<>();
            /*if(employees != null && employees.size() > 0) {
                selectedEmployees = new ArrayList<>();
                for(ErpEmployee e: employees) {
                    selectedEmployees.add(e.getFirstname()+" "+e.getLastname());
                }
            }*/
        } else {
            detachment = new ErpDetachment();
            employees = new ArrayList<>();
            selectedEmployees = new ArrayList<>();
        }
        List<ErpEmployee> employeesList = erpEmployeeService.getAll();
        assignedEmployees = new HashMap<>();
        for(ErpEmployee e: employeesList) {
            assignedEmployees.put(e.getFirstname()+" "+e.getLastname(), e);
        }
    }

    public Long getDetachmentId() {
        return detachmentId;
    }

    public void setDetachmentId(Long detachmentId) {
        this.detachmentId = detachmentId;
    }

    public ErpDetachment getDetachment() {
        return detachment;
    }

    public void setDetachment(ErpDetachment detachment) {
        this.detachment = detachment;
    }

    public List<ErpEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ErpEmployee> employees) {
        this.employees = employees;
    }

    public List<ErpEmployee> getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(List<ErpEmployee> selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public Map<String, ErpEmployee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(Map<String, ErpEmployee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public String backBtnPressed() { return "detachment-form?id="+detachmentId+"faces-redirect=true&includeViewParams=true"; }

    public void save() throws Exception {
        if(selectedEmployees != null && selectedEmployees.size() > 0) {
            if(detachment != null && detachment.getAssignedEmployees() != null) {
                //System.out.println("ASSIGNED EMP NOT NULL");
            }
            else {
                //System.out.println("ASSIGNED EMP NULL");
                detachment.setAssignedEmployees(new HashSet<>());
            }
            for(ErpEmployee emp: selectedEmployees) {
                emp.setErpDetachment(detachment);
                detachment.getAssignedEmployees().add(emp);
                //erpEmployeeService.save(emp);
            }
            //System.out.println("LENGTH: "+detachment.getAssignedEmployees().size());
            //erpEmployeeService.saveAll(selectedEmployees);
            erpDetachmentService.save(detachment);
            addDetailMessage("EMPLOYEES", "ASSIGNED", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+detachment.getId()+"&clientId="+detachment.getErpClient().getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
