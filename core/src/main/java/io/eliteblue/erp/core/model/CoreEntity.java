package io.eliteblue.erp.core.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@MappedSuperclass
public class CoreEntity {

    @Column(name = "UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdate;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dateCreated;

    @Column(name = "OPERATION", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String operation;

    @Column(name = "LAST_EDITED_BY")
    @Size(min = 1, max = 50)
    private String lastEditedBy;

    @Column(name = "CREATED_BY")
    @Size(min = 1, max = 50)
    private String createdBy;

    public String getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
