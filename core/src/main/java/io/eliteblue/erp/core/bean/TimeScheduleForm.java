package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.constants.WorkSchedLegend;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpTimeSchedule;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.TimeScheduleService;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class TimeScheduleForm implements Serializable {

    final DateFormat format = new SimpleDateFormat("HH:mm");

    @Autowired
    private TimeScheduleService timeScheduleService;

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private Long id;
    private Long detachmentId;
    private ErpDetachment detachment;
    private ErpTimeSchedule erpTimeSchedule;
    private Date startTime;
    private Date endTime;

    private Map<String, WorkSchedLegend> workSchedValues;

    public void init() throws Exception {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpTimeSchedule = timeScheduleService.findById(Long.valueOf(id));
            detachment = erpTimeSchedule.getErpDetachment();
            detachmentId = detachment.getId();
            startTime = new Date();
            endTime = new Date();
            startTime.setTime(erpTimeSchedule.getStartTime().getTime());
            endTime.setTime(erpTimeSchedule.getEndTime().getTime());
        } else {
            erpTimeSchedule = new ErpTimeSchedule();
            startTime = new Date();
            endTime = new Date();
            endTime.setTime(25200000);
            startTime.setTime(25200000);
            if(has(detachmentId)) {
                detachment = erpDetachmentService.findById(detachmentId);
                erpTimeSchedule.setErpDetachment(detachment);
            }
        }
        workSchedValues = new HashMap<>();
        for(WorkSchedLegend l: WorkSchedLegend.values()) {
            if(!l.equals(WorkSchedLegend.DO))
                workSchedValues.put(l.name(), l);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ErpTimeSchedule getErpTimeSchedule() {
        return erpTimeSchedule;
    }

    public void setErpTimeSchedule(ErpTimeSchedule erpTimeSchedule) {
        this.erpTimeSchedule = erpTimeSchedule;
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

    public Map<String, WorkSchedLegend> getWorkSchedValues() {
        return workSchedValues;
    }

    public void setWorkSchedValues(Map<String, WorkSchedLegend> workSchedValues) {
        this.workSchedValues = workSchedValues;
    }

    public String backBtnPressed() { return "detachment-form?id="+detachmentId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpTimeSchedule = new ErpTimeSchedule();
        id = null;
    }

    public boolean isNew() {
        return erpTimeSchedule == null || erpTimeSchedule.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpTimeSchedule) && has(erpTimeSchedule.getId())) {
            String name = erpTimeSchedule.getDescription();
            timeScheduleService.delete(erpTimeSchedule);
            addDetailMessage("POST DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+detachment.getId()+"&clientId="+detachment.getErpClient().getId());
        }
    }

    public void save() throws Exception {
        if(erpTimeSchedule != null) {
            erpTimeSchedule.setStartTime(new Time(startTime.getTime()));
            erpTimeSchedule.setEndTime(new Time(endTime.getTime()));
            timeScheduleService.save(erpTimeSchedule);
            addDetailMessage("POST SAVED", erpTimeSchedule.getDescription(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+detachment.getId()+"&clientId="+detachment.getErpClient().getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
