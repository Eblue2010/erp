package io.eliteblue.erp.core.bean;

import com.sun.jdi.LongValue;
import io.eliteblue.erp.core.constants.WorkSchedLegend;
import io.eliteblue.erp.core.model.ErpWorkAssignment;
import io.eliteblue.erp.core.model.ErpWorkDay;
import io.eliteblue.erp.core.service.WorkAssignmentService;
import io.eliteblue.erp.core.service.WorkDayService;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class WorkDayForm implements Serializable {

    @Autowired
    private WorkDayService workDayService;

    @Autowired
    private WorkAssignmentService workAssignmentService;

    private Long id;
    private Long workAssignmentId;
    private ErpWorkAssignment workAssignment;
    private ErpWorkDay erpWorkDay;
    private Map<String, WorkSchedLegend> workSchedValues;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpWorkDay = workDayService.findById(Long.valueOf(id));
            workAssignment = erpWorkDay.getWorkAssignment();
            workAssignmentId = workAssignment.getId();
        } else {
            erpWorkDay = new ErpWorkDay();
            if(has(workAssignmentId)) {
                workAssignment = workAssignmentService.findById(workAssignmentId);
                erpWorkDay.setWorkAssignment(workAssignment);
            }
        }
        workSchedValues = new HashMap<>();
        for(WorkSchedLegend l: WorkSchedLegend.values()) {
            workSchedValues.put(l.name(), l);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkAssignmentId() {
        return workAssignmentId;
    }

    public void setWorkAssignmentId(Long workAssignmentId) {
        this.workAssignmentId = workAssignmentId;
    }

    public ErpWorkAssignment getWorkAssignment() {
        return workAssignment;
    }

    public void setWorkAssignment(ErpWorkAssignment workAssignment) {
        this.workAssignment = workAssignment;
    }

    public ErpWorkDay getErpWorkDay() {
        return erpWorkDay;
    }

    public void setErpWorkDay(ErpWorkDay erpWorkDay) {
        this.erpWorkDay = erpWorkDay;
    }

    public Map<String, WorkSchedLegend> getWorkSchedValues() {
        return workSchedValues;
    }

    public void setWorkSchedValues(Map<String, WorkSchedLegend> workSchedValues) {
        this.workSchedValues = workSchedValues;
    }

    public String backBtnPressed() { return "work-assn-form?id="+workAssignmentId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpWorkDay = new ErpWorkDay();
        id = null;
    }

    public boolean isNew() {
        return erpWorkDay == null || erpWorkDay.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpWorkDay) && has(erpWorkDay.getId())) {
            workDayService.delete(erpWorkDay);
            addDetailMessage("WORK DAY DELETED", "", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("work-assn-form.xhtml?id="+workAssignment.getId());
        }
    }

    public void save() throws Exception {
        if(erpWorkDay != null) {
            workDayService.save(erpWorkDay);
            addDetailMessage("WORK DAY SAVED", "", FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("work-assn-form.xhtml?id="+workAssignment.getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
