package io.eliteblue.erp.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "DETACHMENT")
public class ErpDetachment extends CoreEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detachment_seq")
    @SequenceGenerator(name = "detachment_seq", sequenceName = "detachment_seq", allocationSize = 1, initialValue = 10)
    private Long id;

    @Column(name = "NAME", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @OneToOne
    @JoinColumn(name = "AREA_ID", referencedColumnName = "id")
    private OperationsArea location;

    @OneToMany(mappedBy = "erpDetachment", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<ErpPost> posts;

    @ManyToOne
    @JoinColumn(name = "erp_client_id", nullable = false)
    private ErpClient erpClient;

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

    public OperationsArea getLocation() {
        return location;
    }

    public void setLocation(OperationsArea location) {
        this.location = location;
    }

    public Set<ErpPost> getPosts() {
        return posts;
    }

    public void setPosts(Set<ErpPost> posts) {
        this.posts = posts;
    }

    public ErpClient getErpClient() {
        return erpClient;
    }

    public void setErpClient(ErpClient erpClient) {
        this.erpClient = erpClient;
    }
}
