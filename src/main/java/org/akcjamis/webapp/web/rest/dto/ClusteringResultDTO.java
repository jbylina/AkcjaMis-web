package org.akcjamis.webapp.web.rest.dto;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class ClusteringResultDTO {

    private Integer clusterNum;

    private Long id;

    private String street;

    private String houseNo;

    private String flatNo;

    private String postalcode;

    private String city;

    private Point locationGeom;


    public ClusteringResultDTO() {
    }

    public ClusteringResultDTO(Integer clusterNum, Long id, String street, String houseNo, String flatNo,
                               String postalcode, String city, String locationGeom) {
        this.clusterNum = clusterNum;
        this.id = id;
        this.street = street;
        this.houseNo = houseNo;
        this.flatNo = flatNo;
        this.postalcode = postalcode;
        this.city = city;
        try {
            this.locationGeom = (Point)new WKTReader().read(locationGeom);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(Integer clusterNum) {
        this.clusterNum = clusterNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Point getLocationGeom() {
        return locationGeom;
    }

    public void setLocationGeom(Point locationGeom) {
        this.locationGeom = locationGeom;
    }
}
