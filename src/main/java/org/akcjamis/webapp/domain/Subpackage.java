package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Subpackage.
 */
@Entity
@Table(name = "subpackages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Subpackage extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subpackage_id")
    private Integer id;

    @NotNull
    @Min(value = 1)
    @Column(name = "subpackage_number", nullable = false)
    private Integer subpackageNumber;

    @OneToMany(mappedBy = "subpackage")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action= NotFoundAction.IGNORE)
    private Set<SubpackageNote> subpackageNotes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "christmas_package_id")
    private ChristmasPackage christmasPackage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
