package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "EMPLOYEE_ADDRESS")
public class Address extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "address", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "NAME", length = 200)
    @NotNull
    private String addressName;

    @Column(name = "REGION", length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String region;

    @Column(name = "CITY", length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    @Column(name = "POSTAL_CODE", length = 50)
    @Size(min = 2, max = 50)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private ErpEmployee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public ErpEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(ErpEmployee employee) {
        this.employee = employee;
    }
}
