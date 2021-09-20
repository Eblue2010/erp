package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "ERP_REGION")
public class ErpRegion {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_seq")
    @SequenceGenerator(name = "region_seq", sequenceName = "region", allocationSize = 1, initialValue = 100)
    private Long id;

    @Column(name = "NAME", length = 50, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @OneToMany(mappedBy = "erpRegion", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpCity> cities;

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

    public Set<ErpCity> getCities() {
        return cities;
    }

    public void setCities(Set<ErpCity> cities) {
        this.cities = cities;
    }
}
