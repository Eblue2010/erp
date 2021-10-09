package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ERP_WORK_ASSIGNMENT")
public class ErpWorkAssignment extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workassn_seq")
    @SequenceGenerator(name = "workassn_seq", sequenceName = "workassn_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ASSN_EMP_ID", referencedColumnName = "id")
    @NotNull
    private ErpEmployee employeeAssigned;

    @Column(name = "WORK_ASSIGNMENT", length = 50)
    @NotNull
    private String workAssignment;

    @OneToMany(mappedBy = "workAssignment", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpWorkDay> workDays;

    @ManyToOne
    @JoinColumn(name = "work_schedule_id", nullable = false)
    private ErpWorkSchedule workSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpEmployee getEmployeeAssigned() {
        return employeeAssigned;
    }

    public void setEmployeeAssigned(ErpEmployee employeeAssigned) {
        this.employeeAssigned = employeeAssigned;
    }

    public String getWorkAssignment() {
        return workAssignment;
    }

    public void setWorkAssignment(String workAssignment) {
        this.workAssignment = workAssignment;
    }

    public Set<ErpWorkDay> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Set<ErpWorkDay> workDays) {
        this.workDays = workDays;
    }

    public ErpWorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(ErpWorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }
}
