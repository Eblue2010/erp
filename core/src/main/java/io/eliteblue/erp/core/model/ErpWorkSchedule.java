package io.eliteblue.erp.core.model;

import io.eliteblue.erp.core.constants.ApprovalStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ERP_WORK_SCHEDULE")
public class ErpWorkSchedule extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worksched_seq")
    @SequenceGenerator(name = "worksched_seq", sequenceName = "worksched_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "STOP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stopDate;

    @Column(name = "SHIFT_COMMANDER", length = 50)
    @NotNull
    private String shiftCommander;

    @Column(name = "DETACHMENT_COMMANDER", length = 50)
    @NotNull
    private String detachmentCommander;

    @Column(name = "AREA_SEC_COMMANDER", length = 50)
    @NotNull
    private String areaSecurityCommander;

    @Column(name = "APPROVAL_STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @OneToMany(mappedBy = "workSchedule", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpWorkAssignment> workAssignments;

    @ManyToOne
    @JoinColumn(name = "erp_detachment_id", nullable = false)
    private ErpDetachment erpDetachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public String getShiftCommander() {
        return shiftCommander;
    }

    public void setShiftCommander(String shiftCommander) {
        this.shiftCommander = shiftCommander;
    }

    public String getDetachmentCommander() {
        return detachmentCommander;
    }

    public void setDetachmentCommander(String detachmentCommander) {
        this.detachmentCommander = detachmentCommander;
    }

    public String getAreaSecurityCommander() {
        return areaSecurityCommander;
    }

    public void setAreaSecurityCommander(String areaSecurityCommander) {
        this.areaSecurityCommander = areaSecurityCommander;
    }

    public Set<ErpWorkAssignment> getWorkAssignments() {
        return workAssignments;
    }

    public void setWorkAssignments(Set<ErpWorkAssignment> workAssignments) {
        this.workAssignments = workAssignments;
    }

    public ErpDetachment getErpDetachment() {
        return erpDetachment;
    }

    public void setErpDetachment(ErpDetachment erpDetachment) {
        this.erpDetachment = erpDetachment;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
}
