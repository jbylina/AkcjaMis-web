package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Family.
 */
@Entity
@Table(name = "families")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Family extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int SRID = 4326;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Integer id;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Size(max = 10)
    @Column(name = "house_no", length = 10, nullable = false)
    private String houseNo;

    @Size(max = 10)
    @Column(name = "flat_no", length = 10)
    private String flatNo;

    @Size(max = 6)
    @Column(name = "postalcode", length = 6)
    private String postalcode;

    @Column(name = "district")
    private String district;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "source")
    private String source;

    @Column(name = "location_geom")
    private Point locationGeom;

    @OneToMany(mappedBy = "family")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Child> childs = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action= NotFoundAction.IGNORE)
    private Set<FamilyNote> familyNotes = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChristmasPackage> christmasPackages = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Child> getChilds() {
        return childs;
    }

    public void setChilds(Set<Child> childs) {
        this.childs = childs;
    }

    public Set<FamilyNote> getFamilyNotes() {
        return familyNotes;
    }

    public void setFamilyNotes(Set<FamilyNote> familyNotes) {
        this.familyNotes = familyNotes;
    }

    public Set<ChristmasPackage> getChristmasPackages() {
        return christmasPackages;
    }

    public void setChristmasPackages(Set<ChristmasPackage> christmasPackages) {
        this.christmasPackages = christmasPackages;
    }

    public Point getLocationGeom() {
        return locationGeom;
    }

    public void setLocationGeom(Point locationGeom) {
        locationGeom.setSRID(SRID);
        this.locationGeom = locationGeom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Family family = (Family) o;
        if(family.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, family.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Family{" +
            "id=" + id +
            ", street='" + street + "'" +
            ", houseNo='" + houseNo + "'" +
            ", flatNo='" + flatNo + "'" +
            ", postalcode='" + postalcode + "'" +
            ", district='" + district + "'" +
            ", city='" + city + "'" +
            ", region='" + region + "'" +
            ", source='" + source + "'" +
            '}';
    }
}
