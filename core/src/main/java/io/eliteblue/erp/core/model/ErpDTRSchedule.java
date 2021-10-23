package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ERP_DTR_SCHEDULE")
public class ErpDTRSchedule extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dtr_sched_seq")
    @SequenceGenerator(name = "dtr_sched_seq", sequenceName = "dtr_sched_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "SHIFT_START")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date shiftStart;

    @Column(name = "SHIFT_END")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date shiftEnd;

    @ManyToOne
    @JoinColumn(name = "dtr_assn_id", nullable = false)
    private ErpDTRAssignment erpDTRAssignment;

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

    public ErpDTRAssignment getErpDTRAssignment() {
        return erpDTRAssignment;
    }

    public void setErpDTRAssignment(ErpDTRAssignment erpDTRAssignment) {
        this.erpDTRAssignment = erpDTRAssignment;
    }
}
