package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Subpackage.
 */
@Entity
@Table(name = "subpackages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subpackage")
@Indexed
public class Subpackage extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "subpackage_number", nullable = false)
    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO)
    private Integer subpackageNumber;

    @OneToMany(mappedBy = "subpackage")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SubpackageNote> subpackageNotes = new HashSet<>();

    @ManyToOne
    private Child child;

    @ManyToOne
    @JsonIgnore
    private ChristmasPackage christmasPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSubpackageNumber() {
        return subpackageNumber;
    }

    public void setSubpackageNumber(Integer subpackageNumber) {
        this.subpackageNumber = subpackageNumber;
    }

    public Set<SubpackageNote> getSubpackageNotes() {
        return subpackageNotes;
    }

    public void setSubpackageNotes(Set<SubpackageNote> subpackageNotes) {
        this.subpackageNotes = subpackageNotes;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public ChristmasPackage getChristmasPackage() {
        return christmasPackage;
    }

    public void setChristmasPackage(ChristmasPackage christmasPackage) {
        this.christmasPackage = christmasPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subpackage subpackage = (Subpackage) o;
        if(subpackage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subpackage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Subpackage{" +
            "id=" + id +
            ", subpackageNumber='" + subpackageNumber + "'" +
            '}';
    }
}
