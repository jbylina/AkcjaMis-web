package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.akcjamis.webapp.domain.enumeration.Sex;

/**
 * A Child.
 */
@Entity
@Table(name = "children")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "child")
public class Child extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotNull
    @Size(max = 255)
    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @NotNull
    @Column(name = "birth_year", nullable = false)
    private LocalDate birthYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Family family;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(LocalDate birthYear) {
        this.birthYear = birthYear;
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
        Child child = (Child) o;
        if(child.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, child.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Child{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", sex='" + sex + "'" +
            ", birthYear='" + birthYear + "'" +
            '}';
    }
}
