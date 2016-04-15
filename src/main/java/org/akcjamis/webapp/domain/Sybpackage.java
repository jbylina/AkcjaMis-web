package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sybpackage.
 */
@Entity
@Table(name = "sybpackage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sybpackage")
public class Sybpackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sbk_id")
    private Integer sbkID;

    @ManyToOne
    private PackageGift pack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSbkID() {
        return sbkID;
    }

    public void setSbkID(Integer sbkID) {
        this.sbkID = sbkID;
    }

    public PackageGift getPack() {
        return pack;
    }

    public void setPack(PackageGift packageGift) {
        this.pack = packageGift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sybpackage sybpackage = (Sybpackage) o;
        if(sybpackage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sybpackage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sybpackage{" +
            "id=" + id +
            ", sbkID='" + sbkID + "'" +
            '}';
    }
}
