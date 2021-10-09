package io.eliteblue.erp.core.model;


import io.eliteblue.erp.core.constants.WorkSchedLegend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ERP_WORK_DAY")
public class ErpWorkDay extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workday_seq")
    @SequenceGenerator(name = "workday_seq", sequenceName = "workday_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "SHIFT_START")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date shiftStart;

    @Column(name = "SHIFT_END")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date shiftEnd;

    @Column(name = "SHIFT_SCHED", length = 20)
    @Enumerated(EnumType.STRING)
    private WorkSchedLegend shiftSchedule;

    @ManyToOne
    @JoinColumn(name = "work_assignment_id", nullable = false)
    private ErpWorkAssignment workAssignment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(Date shiftStart) {
        this.shiftStart = shiftStart;
    }

    public Date getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(Date shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public WorkSchedLegend getShiftSchedule() {
        return shiftSchedule;
    }

    public void setShiftSchedule(WorkSchedLegend shiftSchedule) {
        this.shiftSchedule = shiftSchedule;
    }

    public ErpWorkAssignment getWorkAssignment() {
        return workAssignment;
    }

    public void setWorkAssignment(ErpWorkAssignment workAssignment) {
        this.workAssignment = workAssignment;
    }
}
