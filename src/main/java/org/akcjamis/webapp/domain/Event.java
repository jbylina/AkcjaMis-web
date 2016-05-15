package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Event table
 */
@Entity
@Table(name = "events")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "event_year", nullable = false, unique = true)
    private Short year;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChristmasPackage> christmasPackages = new HashSet<>();

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<ChristmasPackage> getChristmasPackages() {
        return christmasPackages;
    }

    public void setChristmasPackages(Set<ChristmasPackage> christmasPackages) {
        this.christmasPackages = christmasPackages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if(event.year == null || year == null) {
            return false;
        }
        return Objects.equals(year, event.year);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(year);
    }

    @Override
    public String toString() {
        return "Event{" +
            ", year='" + year + "'" +
            '}';
    }
}
