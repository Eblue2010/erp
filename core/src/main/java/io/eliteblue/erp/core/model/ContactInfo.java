package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "EMPLOYEE_CONTACT")
public class ContactInfo {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_seq")
    @SequenceGenerator(name = "contact_seq", sequenceName = "contact", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "CONTACT_NUMBER", length = 50, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    private String contactNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private ErpEmployee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public ErpEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(ErpEmployee employee) {
        this.employee = employee;
    }
}
