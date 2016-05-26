package org.akcjamis.webapp.repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ClusteredFamilyRepository
{
    private static final String QUERY_PATTERN = "SELECT ST_AsText(UnNest(ST_ClusterWithin(location_geom, %f))) FROM families";

    private final WKTReader wktReader = new WKTReader();
    private final EntityManager entityManager;

    @Inject
    ClusteredFamilyRepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public List<List<Geometry>> clusterFamiliesWithin(double distance)
    {
        return (List<List<Geometry>>) entityManager
            .createNativeQuery(String.format(QUERY_PATTERN, distance))
            .getResultList()
            .stream()
            .map(o -> tryAdapt(o))
            .collect(Collectors.<List<Geometry>>toList());
    }

    private List<Geometry> tryAdapt(Object o)
    {
        try
        {
            return adapt((String) o);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    private List<Geometry> adapt(String o) throws ParseException
    {
        String wellKnownText = o;
        Geometry geometry = wktReader.read(wellKnownText);
        GeometryCollection geometryCollection = (GeometryCollection) geometry;

        int numGeometries = geometryCollection.getNumGeometries();
        List<Geometry> geometries = new ArrayList<>(numGeometries);

        for(int i = 0; i < numGeometries; i++)
        {
            geometries.add(geometryCollection.getGeometryN(i));
        }

        return geometries;
    }
}
