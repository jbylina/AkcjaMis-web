package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ChristmasPackage.
 */
@Entity
@Table(name = "christmas_packages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "christmaspackage")
public class ChristmasPackage extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "mark")
    private Integer mark;

    @Column(name = "delivered", nullable = false)
    private Boolean delivered = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "event_year")
    private Event event;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "christmasPackage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChristmasPackageNote> christmasPackageNotes = new HashSet<>();

    @OneToMany(mappedBy = "christmasPackage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChristmasPackageChange> christmasPackageChanges = new HashSet<>();

    @OneToMany(mappedBy = "christmasPackage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Subpackage> subpackages = new HashSet<>();

    @ManyToOne
    private Family family;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<ChristmasPackageNote> getChristmasPackageNotes() {
        return christmasPackageNotes;
    }

    public void setChristmasPackageNotes(Set<ChristmasPackageNote> christmasPackageNotes) {
        this.christmasPackageNotes = christmasPackageNotes;
    }

    public Set<ChristmasPackageChange> getChristmasPackageChanges() {
        return christmasPackageChanges;
    }

    public void setChristmasPackageChanges(Set<ChristmasPackageChange> christmasPackageChanges) {
        this.christmasPackageChanges = christmasPackageChanges;
    }

    public Set<Subpackage> getSubpackages() {
        return subpackages;
    }

    public void setSubpackages(Set<Subpackage> subpackages) {
        this.subpackages = subpackages;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChristmasPackage christmasPackage = (ChristmasPackage) o;
        if(christmasPackage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, christmasPackage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChristmasPackage{" +
            "id=" + id +
            ", mark='" + mark + "'" +
            ", delivered='" + delivered + "'" +
            '}';
    }
}
