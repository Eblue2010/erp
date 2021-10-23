package io.eliteblue.erp.core.model;

import io.eliteblue.erp.core.constants.WorkSchedLegend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;

@Entity
@Table(name = "ERP_TIME_SCHEDULE")
public class ErpTimeSchedule extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_sh_seq")
    @SequenceGenerator(name = "time_sh_seq", sequenceName = "time_sh_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "DESCRIPTION", length = 50)
    String description;

    @Column(name = "START_TIME")
    @NotNull
    private Time startTime;

    @Column(name = "END_TIME")
    @NotNull
    private Time endTime;

    @Column(name = "SCHEDULE_LEGEND", length = 20)
    @Enumerated(EnumType.STRING)
    private WorkSchedLegend legend;

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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public ErpDetachment getErpDetachment() {
        return erpDetachment;
    }

    public void setErpDetachment(ErpDetachment erpDetachment) {
        this.erpDetachment = erpDetachment;
    }

    public WorkSchedLegend getLegend() {
        return legend;
    }

    public void setLegend(WorkSchedLegend legend) {
        this.legend = legend;
    }
}
