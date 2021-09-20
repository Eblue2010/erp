package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ERP_CITY")
public class ErpCity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_seq")
    @SequenceGenerator(name = "city_seq", sequenceName = "city", allocationSize = 1, initialValue = 100)
    private Long id;

    @Column(name = "NAME", length = 50, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private ErpRegion erpRegion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ErpRegion getErpRegion() {
        return erpRegion;
    }

    public void setErpRegion(ErpRegion erpRegion) {
        this.erpRegion = erpRegion;
    }
}
