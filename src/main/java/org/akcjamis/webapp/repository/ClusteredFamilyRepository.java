package org.akcjamis.webapp.repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.akcjamis.webapp.domain.Family;
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
    private final FamilyRepository familyRepository;

    @Inject
    ClusteredFamilyRepository(EntityManager entityManager, FamilyRepository familyRepository)
    {
        this.entityManager = entityManager;
        this.familyRepository = familyRepository;
    }

    public List<List<Family>> clusterFamiliesWithin(double distance)
    {
        return (List<List<Family>>) entityManager
            .createNativeQuery(String.format(QUERY_PATTERN, distance))
            .getResultList()
            .stream()
            .map(o -> tryRecognizeFamily(o))
            .collect(Collectors.<List<Geometry>>toList());
    }

    private List<Family> tryRecognizeFamily(Object o)
    {
        try
        {
            return recognizeFamily(o);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    private List<Family> recognizeFamily(Object o) throws ParseException
    {
        String wellKnownText = (String) o;
        Geometry geometry = wktReader.read(wellKnownText);
        GeometryCollection geometryCollection = (GeometryCollection) geometry;

        int numGeometries = geometryCollection.getNumGeometries();
        List<Family> families = new ArrayList<>(numGeometries);

        for(int i = 0; i < numGeometries; i++)
        {
            Geometry geometryN = geometryCollection.getGeometryN(i);
            geometryN.setSRID(Family.SRID);

            Family family = familyRepository.findByLocationGeom(geometryN);

            families.add(family);
        }

        return families;
    }
}
