package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ERP_ID_TYPE")
public class ErpIDType extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_type_seq")
    @SequenceGenerator(name = "id_type_seq", sequenceName = "id_type_seq", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "NAME", length = 50, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    String name;

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
}
