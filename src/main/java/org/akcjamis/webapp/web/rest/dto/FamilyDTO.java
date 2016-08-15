package org.akcjamis.webapp.web.rest.dto;

import com.vividsolutions.jts.geom.Point;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class FamilyDTO extends AbstractAuditingDTO {

    private static final int SRID = 4326;

    private Integer id;

    @NotNull
    private String street;

    @NotNull
    @Size(max = 10)
    private String houseNo;

    @Size(max = 10)
    private String flatNo;

    @Size(max = 6)
    private String postalcode;

    private String district;

    @NotNull
    private String city;

    private String region;

    private String source;

    private Point locationGeom;

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
        FamilyDTO family = (FamilyDTO) o;
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
        return "FamilyDTO{" +
            "id=" + id +
            ", street='" + street + "'" +
            ", houseNo='" + houseNo + "'" +
            ", flatNo='" + flatNo + "'" +
            ", postalcode='" + postalcode + "'" +
            ", district='" + district + "'" +
            ", city='" + city + "'" +
            ", region='" + region + "'" +
            ", source='" + source + "'" +
            ", locationGeom='" + locationGeom + "'" +
            '}';
    }
}
