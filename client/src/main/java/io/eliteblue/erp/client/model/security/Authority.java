package io.eliteblue.erp.client.model.security;

import io.eliteblue.erp.core.model.CoreEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "AUTHORITY")
public class Authority extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
    @SequenceGenerator(name = "authority_seq", sequenceName = "authority_seq", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "NAME", length = 50, unique = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<ErpUser> erpUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    public List<ErpUser> getErpUsers() {
        return erpUsers;
    }

    public void setErpUsers(List<ErpUser> erpUsers) {
        this.erpUsers = erpUsers;
    }
}