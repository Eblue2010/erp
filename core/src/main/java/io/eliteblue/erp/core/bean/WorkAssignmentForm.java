package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.ErpWorkAssignment;
import io.eliteblue.erp.core.model.ErpWorkDay;
import io.eliteblue.erp.core.model.ErpWorkSchedule;
import io.eliteblue.erp.core.service.ErpEmployeeService;
import io.eliteblue.erp.core.service.WorkAssignmentService;
import io.eliteblue.erp.core.service.WorkScheduleService;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class WorkAssignmentForm implements Serializable {

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @Autowired
    private WorkScheduleService workScheduleService;

    @Autowired
    private ErpEmployeeService employeeService;

    private Long id;
    private Long workScheduleId;
    private ErpWorkSchedule workSchedule;
    private ErpWorkAssignment erpWorkAssignment;

    private ErpEmployee employee;
    private Map<String, Long> employees;

    private List<ErpWorkDay> filteredErpWorkDay;
    private List<ErpWorkDay> workDays;
    private ErpWorkDay selectedWorkDay;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpWorkAssignment = workAssignmentService.findById(Long.valueOf(id));
            workSchedule = erpWorkAssignment.getWorkSchedule();
            workScheduleId = workSchedule.getId();
            employee = erpWorkAssignment.getEmployeeAssigned();
            workDays = new ArrayList<>(erpWorkAssignment.getWorkDays());
        } else {
            erpWorkAssignment = new ErpWorkAssignment();
            employee = new ErpEmployee();
            if(has(workScheduleId)) {
                workSchedule = workScheduleService.findById(workScheduleId);
                erpWorkAssignment.setWorkSchedule(workSchedule);
            }
        }
        employees = new HashMap<>();
        for(ErpEmployee emp: employeeService.getAllFiltered()) {
            employees.put(emp.getFirstname()+" "+emp.getLastname(), emp.getId());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(Long workScheduleId) {
        this.workScheduleId = workScheduleId;
    }

    public ErpWorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(ErpWorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public ErpWorkAssignment getErpWorkAssignment() {
        return erpWorkAssignment;
    }

    public void setErpWorkAssignment(ErpWorkAssignment erpWorkAssignment) {
        this.erpWorkAssignment = erpWorkAssignment;
    }

    public ErpEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(ErpEmployee employee) {
        this.employee = employee;
    }

    public Map<String, Long> getEmployees() {
        return employees;
    }

    public void setEmployees(Map<String, Long> employees) {
        this.employees = employees;
    }

    public List<ErpWorkDay> getFilteredErpWorkDay() {
        return filteredErpWorkDay;
    }

    public void setFilteredErpWorkDay(List<ErpWorkDay> filteredErpWorkDay) {
        this.filteredErpWorkDay = filteredErpWorkDay;
    }

    public List<ErpWorkDay> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<ErpWorkDay> workDays) {
        this.workDays = workDays;
    }

    public ErpWorkDay getSelectedWorkDay() {
        return selectedWorkDay;
    }

    public void setSelectedWorkDay(ErpWorkDay selectedWorkDay) {
        this.selectedWorkDay = selectedWorkDay;
    }

    public void onRowSelect(SelectEvent<ErpWorkDay> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("workday-form.xhtml?id="+selectedWorkDay.getId()+"&workAssignmentId="+id);
    }

    public void onRowUnselect(UnselectEvent<ErpWorkDay> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("workday-form.xhtml?id="+selectedWorkDay.getId()+"&workAssignmentId="+id);
    }

    public String newWorkDayPressed() {
        return "workday-form?workAssignmentId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public String backBtnPressed() { return "workschedule-form?id="+workScheduleId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpWorkAssignment = new ErpWorkAssignment();
        id = null;
    }

    public boolean isNew() {
        return erpWorkAssignment == null || erpWorkAssignment.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpWorkAssignment) && has(erpWorkAssignment.getId())) {
            String name = erpWorkAssignment.getWorkAssignment();
            workAssignmentService.delete(erpWorkAssignment);
            addDetailMessage("WORK ASSIGNMENT DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("workschedule-form.xhtml?id="+workSchedule.getId());
        }
    }

    public void save() throws Exception {
        if(erpWorkAssignment != null) {
            if(has(employee.getId())) {
                employee = employeeService.findById(employee.getId());
                erpWorkAssignment.setEmployeeAssigned(employee);
            }
            workAssignmentService.save(erpWorkAssignment);
            addDetailMessage("WORK ASSIGNMENT SAVED", erpWorkAssignment.getWorkAssignment(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("workschedule-form.xhtml?id="+workSchedule.getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
