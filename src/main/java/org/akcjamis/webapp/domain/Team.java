package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Team.
 */
@Entity
@Table(name = "teams")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "team_id")
    private Integer id;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "team_number", nullable = false)
    private Integer teamNumber;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "team_users",
               joinColumns = @JoinColumn(name="team_id", referencedColumnName="team_id"),
               inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="ID"))
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "event_year")
    private Event event;

    @OneToMany(mappedBy = "team")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChristmasPackage> christmasPackages = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
        Team team = (Team) o;
        if(team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", teamNumber='" + teamNumber + "'" +
            ", note='" + note + "'" +
            '}';
    }
}
