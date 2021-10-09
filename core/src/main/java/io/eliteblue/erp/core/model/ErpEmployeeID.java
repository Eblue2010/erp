package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ERP_EMPLOYEE_IDENTIFICATION")
public class ErpEmployeeID extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "erp_emp_id_seq")
    @SequenceGenerator(name = "erp_emp_id_seq", sequenceName = "erp_emp_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "ID_NO", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String identificationNumber;

    @OneToOne
    @JoinColumn(name = "EMP_ID_TYPE", referencedColumnName = "id")
    @NotNull
    private ErpIDType idName;

    @ManyToOne
    @JoinColumn(name = "erp_employee_id", nullable = false)
    private ErpEmployee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public ErpIDType getIdName() {
        return idName;
    }

    public void setIdName(ErpIDType idName) {
        this.idName = idName;
    }

    public ErpEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(ErpEmployee employee) {
        this.employee = employee;
    }
}
