package io.eliteblue.erp.core.model;

import io.eliteblue.erp.core.constants.BloodType;
import io.eliteblue.erp.core.constants.CivilStatus;
import io.eliteblue.erp.core.constants.EmployeeStatus;
import io.eliteblue.erp.core.constants.Gender;
import io.eliteblue.erp.core.generator.EmployeeIDGenerator;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "ERP_EMPLOYEE")
public class ErpEmployee extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "EMP_ID", length = 50, updatable = false, unique = true)
    private String employeeId;

    @Column(name = "FIRSTNAME", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String lastname;

    @Column(name = "MIDDLENAME", length = 50)
    private String middlename;

    @Column(name = "EXTNAME", length = 50)
    private String extname;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "JOINED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedDate;

    @Column(name = "RESIGNED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resignedDate;

    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(name = "BIRTH_PLACE", length = 50)
    private String birthPlace;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @Column(name = "GENDER", length = 20)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "CIVIL_STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private CivilStatus civilStatus;

    @Column(name = "BLOOD_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name="WEIGHT_KG", precision = 5, scale = 2)
    private Double weightKilo;

    @Column(name="WEIGHT_LB", precision = 5, scale = 2)
    private Double weightPound;

    @Column(name = "HEIGHT_CM")
    private Double heightCentimeters;

    @Column(name = "MARKS", length = 50)
    private String marks;

    @Column(name = "COMPANY_POSITION", length = 50)
    private String position;

    @Column(name = "BANK_NAME", length = 50)
    private String bankName;

    @Column(name = "BANK_ACC_NUM", length = 50)
    private String bankAccountNumber;

    @OneToOne
    @JoinColumn(name = "AREA_ID", referencedColumnName = "id")
    private OperationsArea assignedLocation;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ContactInfo> contacts;

    @ManyToOne
    @JoinColumn(name = "erp_detachment_id")
    private ErpDetachment erpDetachment;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpEmployeeID> erpEmployeeIDList;

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getExtname() {
        return extname;
    }

    public void setExtname(String extname) {
        this.extname = extname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Date getResignedDate() {
        return resignedDate;
    }

    public void setResignedDate(Date resignedDate) {
        this.resignedDate = resignedDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public Double getWeightKilo() {
        return weightKilo;
    }

    public void setWeightKilo(Double weightKilo) {
        this.weightKilo = weightKilo;
    }

    public Double getWeightPound() {
        return weightPound;
    }

    public void setWeightPound(Double weightPound) {
        this.weightPound = weightPound;
    }

    public Double getHeightCentimeters() {
        return heightCentimeters;
    }

    public void setHeightCentimeters(Double heightCentimeters) {
        this.heightCentimeters = heightCentimeters;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<ContactInfo> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactInfo> contacts) {
        this.contacts = contacts;
    }

    public ErpDetachment getErpDetachment() {
        return erpDetachment;
    }

    public void setErpDetachment(ErpDetachment erpDetachment) {
        this.erpDetachment = erpDetachment;
    }

    public OperationsArea getAssignedLocation() {
        return assignedLocation;
    }

    public void setAssignedLocation(OperationsArea assignedLocation) {
        this.assignedLocation = assignedLocation;
    }

    public Set<ErpEmployeeID> getErpEmployeeIDList() {
        return erpEmployeeIDList;
    }

    public void setErpEmployeeIDList(Set<ErpEmployeeID> erpEmployeeIDList) {
        this.erpEmployeeIDList = erpEmployeeIDList;
    }
}
