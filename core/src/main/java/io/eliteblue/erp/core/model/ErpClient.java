package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "ERP_CLIENT")
public class ErpClient extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "NAME", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(mappedBy = "erpClient", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpDetachment> erpDetachments;

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

    public Set<ErpDetachment> getErpDetachments() {
        return erpDetachments;
    }

    public void setErpDetachments(Set<ErpDetachment> erpDetachments) {
        this.erpDetachments = erpDetachments;
    }
}
