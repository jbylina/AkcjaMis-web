package org.akcjamis.webapp.web.rest.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.List;
import java.util.stream.Collectors;

public class RouteDTO {

    private List<Integer> optimalOrder;

    private List<Geometry> routePaths;

    public RouteDTO(List<Integer> optimalOrder, List<String> routePaths) {
        this.optimalOrder = optimalOrder;
        WKTReader reader = new WKTReader();

        this.routePaths = routePaths.stream()
            .filter(p -> p != null)
            .map(s -> { try {
                return reader.read(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }})
            .collect(Collectors.toList());
    }

    public List<Integer> getOptimalOrder() {
        return optimalOrder;
    }

    public void setOptimalOrder(List<Integer> optimalOrder) {
        this.optimalOrder = optimalOrder;
    }

    public List<Geometry> getRoutePaths() {
        return routePaths;
    }

    public void setRoutePaths(List<Geometry> routePaths) {
        this.routePaths = routePaths;
    }
}
