package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.constants.ApprovalStatus;
import io.eliteblue.erp.core.constants.WorkSchedLegend;
import io.eliteblue.erp.core.model.*;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.WorkAssignmentService;
import io.eliteblue.erp.core.service.WorkDayService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class WorkScheduleForm implements Serializable {

    @Autowired
    private WorkScheduleService workScheduleService;

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @Autowired
    private WorkDayService workDayService;

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

    private List<String> workingDays;
    private LocalDate startDate;
    private LocalDate stopDate;

    public void init() {
        workingDays = new ArrayList<>();
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpWorkSchedule = workScheduleService.findById(Long.valueOf(id));
            detachment = erpWorkSchedule.getErpDetachment();
            detachmentId = detachment.getId();
            if(erpWorkSchedule != null && erpWorkSchedule.getWorkAssignments() != null && erpWorkSchedule.getWorkAssignments().size() > 0) {
                workAssignments = new ArrayList<>(erpWorkSchedule.getWorkAssignments());
                DateFormat format = new SimpleDateFormat("dd");
                workAssignments.sort(Comparator.comparing((ErpWorkAssignment ewa) -> ewa.getEmployeeAssigned().getLastname()));
                for(ErpWorkAssignment wa: workAssignments) {
                    List<ErpWorkDay> wrkDays = new ArrayList<>(wa.getWorkDays());
                    wrkDays.sort(Comparator.comparing((ErpWorkDay wd) -> wd.getShiftStart()));
                    wa.setWorkDays(new LinkedHashSet<>(wrkDays));
                }
            }
            if(has(erpWorkSchedule.getStartDate()) && has(erpWorkSchedule.getStopDate())) {
                startDate = convertToLocalDateViaInstant(erpWorkSchedule.getStartDate());
                stopDate = convertToLocalDateViaInstant(erpWorkSchedule.getStopDate());
                for (LocalDate date = startDate; (date.isBefore(stopDate) || date.isEqual(stopDate)); date = date.plusDays(1)) {
                    workingDays.add(""+date.getDayOfMonth());
                }
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
        for(ErpDetachment edp: erpDetachmentService.getAllFilteredLocation()) {
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

    public List<String> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDate stopDate) {
        this.stopDate = stopDate;
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

    public String approve() {
        if(erpWorkSchedule != null) {
            erpWorkSchedule.setStatus(ApprovalStatus.APPROVED);
            workScheduleService.save(erpWorkSchedule);
        }
        return "workschedules?faces-redirect=true&includeViewParams=true";
    }

    public String reject() {
        if(erpWorkSchedule != null) {
            erpWorkSchedule.setStatus(ApprovalStatus.REJECTED);
            workScheduleService.save(erpWorkSchedule);
        }
        return "workschedules?faces-redirect=true&includeViewParams=true";
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
            Set<ErpWorkAssignment> workAssignments = new HashSet<>();
            Set<ErpWorkDay> workDays = new HashSet<>();

            if(has(detachment.getId())) {
                detachment = erpDetachmentService.findById(detachment.getId());
                erpWorkSchedule.setErpDetachment(detachment);
                erpWorkSchedule.setStatus(ApprovalStatus.PENDING);
                if(has(detachment) && erpWorkSchedule.getId() == null) {
                    // generate work assignment
                    if(has(detachment.getAssignedEmployees()) && detachment.getAssignedEmployees().size() > 0) {
                        for(ErpEmployee emp: detachment.getAssignedEmployees()) {
                            ErpWorkAssignment workAssignment = new ErpWorkAssignment();
                            workAssignment.setEmployeeAssigned(emp);
                            workAssignment.setWorkSchedule(erpWorkSchedule);
                            workAssignment.setWorkAssignment(emp.getPosition());

                            // generate workdays
                            LocalDate _startDate = convertToLocalDateViaInstant(erpWorkSchedule.getStartDate());
                            LocalDate _stopDate = convertToLocalDateViaInstant(erpWorkSchedule.getStopDate());
                            for (LocalDate date = _startDate; (date.isBefore(_stopDate) || date.isEqual(_stopDate)); date = date.plusDays(1)) {
                                ErpWorkDay workDay = new ErpWorkDay();
                                workDay.setWorkAssignment(workAssignment);
                                if(has(detachment.getErpTimeSchedules()) && detachment.getErpTimeSchedules().size() > 0) {
                                    ErpTimeSchedule timeSchedule = detachment.getErpTimeSchedules().iterator().next();
                                    LocalDateTime ldt_start = date.atTime(timeSchedule.getStartTime().toLocalTime());
                                    LocalDateTime ldt_stop = date.atTime(timeSchedule.getEndTime().toLocalTime());
                                    workDay.setShiftStart(convertToDateViaInstant(ldt_start));
                                    workDay.setShiftEnd(convertToDateViaInstant(ldt_stop));
                                    if(timeSchedule.getStartTime().toLocalTime().getHour() < 12) {
                                        workDay.setShiftSchedule(WorkSchedLegend.DS);
                                    } else {
                                        workDay.setShiftSchedule(WorkSchedLegend.NS);
                                    }
                                }
                                workDays.add(workDay);
                            }
                            workAssignment.setWorkDays(workDays);
                            workAssignments.add(workAssignment);
                        }
                    }
                    erpWorkSchedule.setWorkAssignments(workAssignments);
                }
            }
            workScheduleService.save(erpWorkSchedule);
            for(ErpWorkAssignment wa: workAssignments) {
                workAssignmentService.save(wa);
            }
            for(ErpWorkDay wd: workDays) {
                workDayService.save(wd);
            }
            addDetailMessage("SCHEDULE SAVED", erpWorkSchedule.getDescription(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("workschedules.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
