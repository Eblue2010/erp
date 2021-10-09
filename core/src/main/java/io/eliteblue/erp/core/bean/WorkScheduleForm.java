package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.model.ErpWorkAssignment;
import io.eliteblue.erp.core.model.ErpWorkSchedule;
import io.eliteblue.erp.core.service.ErpDetachmentService;
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
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class WorkScheduleForm implements Serializable {

    @Autowired
    private WorkScheduleService workScheduleService;

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private Long id;
    private Long detachmentId;
    private ErpDetachment detachment;
    private ErpWorkSchedule erpWorkSchedule;
    private Map<String, Long> detachments;

    private List<ErpWorkAssignment> filteredErpWorkAssignment;
    private List<ErpWorkAssignment> workAssignments;
    private ErpWorkAssignment selectedWorkAssignment;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpWorkSchedule = workScheduleService.findById(Long.valueOf(id));
            detachment = erpWorkSchedule.getErpDetachment();
            detachmentId = detachment.getId();
            if(erpWorkSchedule != null && erpWorkSchedule.getWorkAssignments() != null && erpWorkSchedule.getWorkAssignments().size() > 0) {
                workAssignments = new ArrayList<>(erpWorkSchedule.getWorkAssignments());
            }
        } else {
            erpWorkSchedule = new ErpWorkSchedule();
            detachment = new ErpDetachment();
            if(has(detachmentId)) {
                detachment = erpDetachmentService.findById(detachmentId);
                erpWorkSchedule.setErpDetachment(detachment);
            }
        }
        detachments = new HashMap<>();
        for(ErpDetachment edp: erpDetachmentService.getAll()) {
            detachments.put(edp.getName(), edp.getId());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpWorkSchedule getErpWorkSchedule() {
        return erpWorkSchedule;
    }

    public void setErpWorkSchedule(ErpWorkSchedule erpWorkSchedule) {
        this.erpWorkSchedule = erpWorkSchedule;
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

    public Map<String, Long> getDetachments() {
        return detachments;
    }

    public void setDetachments(Map<String, Long> detachments) {
        this.detachments = detachments;
    }

    public List<ErpWorkAssignment> getFilteredErpWorkAssignment() {
        return filteredErpWorkAssignment;
    }

    public void setFilteredErpWorkAssignment(List<ErpWorkAssignment> filteredErpWorkAssignment) {
        this.filteredErpWorkAssignment = filteredErpWorkAssignment;
    }

    public List<ErpWorkAssignment> getWorkAssignments() {
        return workAssignments;
    }

    public void setWorkAssignments(List<ErpWorkAssignment> workAssignments) {
        this.workAssignments = workAssignments;
    }

    public ErpWorkAssignment getSelectedWorkAssignment() {
        return selectedWorkAssignment;
    }

    public void setSelectedWorkAssignment(ErpWorkAssignment selectedWorkAssignment) {
        this.selectedWorkAssignment = selectedWorkAssignment;
    }

    public void onRowSelect(SelectEvent<ErpWorkAssignment> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("work-assn-form.xhtml?id="+selectedWorkAssignment.getId()+"&workScheduleId="+id);
    }

    public void onRowUnselect(UnselectEvent<ErpWorkAssignment> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("work-assn-form.xhtml?id="+selectedWorkAssignment.getId()+"&workScheduleId="+id);
    }

    public String backBtnPressed() { return "detachment-form?id="+detachmentId+"faces-redirect=true&includeViewParams=true"; }

    public String newAssignmentPressed() {
        return "work-assn-form?workScheduleId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public void clear() {
        erpWorkSchedule = new ErpWorkSchedule();
        id = null;
    }

    public boolean isNew() {
        return erpWorkSchedule == null || erpWorkSchedule.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpWorkSchedule) && has(erpWorkSchedule.getId())) {
            String name = erpWorkSchedule.getDescription();
            workScheduleService.delete(erpWorkSchedule);
            addDetailMessage("SCHEDULE DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("workschedules.xhtml");
        }
    }

    public void save() throws Exception {
        if(erpWorkSchedule != null) {
            if(has(detachment.getId())) {
                detachment = erpDetachmentService.findById(detachment.getId());
                erpWorkSchedule.setErpDetachment(detachment);
            }
            workScheduleService.save(erpWorkSchedule);
            addDetailMessage("SCHEDULE SAVED", erpWorkSchedule.getDescription(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("workschedules.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
