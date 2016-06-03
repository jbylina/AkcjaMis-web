package org.akcjamis.webapp.web.rest.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.List;
import java.util.stream.Collectors;

public class RouteDTO {

    private List<Long> optimalOrder;

    private List<Geometry> routePaths;

    public RouteDTO(List<Long> optimalOrder, List<String> routePaths) {
        this.optimalOrder = optimalOrder;
        WKTReader reader = new WKTReader();

        this.routePaths = routePaths.stream()
            .map(s -> { try {
                return reader.read(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }})
            .collect(Collectors.toList());
    }

    public List<Long> getOptimalOrder() {
        return optimalOrder;
    }

    public void setOptimalOrder(List<Long> optimalOrder) {
        this.optimalOrder = optimalOrder;
    }

    public List<Geometry> getRoutePaths() {
        return routePaths;
    }

    public void setRoutePaths(List<Geometry> routePaths) {
        this.routePaths = routePaths;
    }
}
