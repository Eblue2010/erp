package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "AREA")
public class OperationsArea extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_seq")
    @SequenceGenerator(name = "area_seq", sequenceName = "area_seq", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "LOCATION", length = 50, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
