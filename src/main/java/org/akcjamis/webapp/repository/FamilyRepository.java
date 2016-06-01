package org.akcjamis.webapp.repository;

import com.vividsolutions.jts.geom.Geometry;
import org.akcjamis.webapp.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family,Long> {

    Family findByLocationGeom(Geometry geometry);

    @Query(value = "SELECT clst.clst_num as cluster_num, " +
                   "f.id," +
                   "f.street," +
                   "f.house_no," +
                   "f.flat_no," +
                   "f.postalcode," +
                   "f.city," +
                   "st_astext(f.location_geom) location_geom" +
                   "  FROM (SELECT row_number() OVER () clst_num\n" +
                   "              ,(st_DumpPoints(clst.geom)).geom fam_geom\n" +
                   "            FROM (SELECT unnest(ST_ClusterWithin(location_geom, ?1)) geom\n" +
                   "                  FROM families\n" +
                   "                  WHERE location_geom IS NOT NULL) clst) clst\n" +
                   "LEFT JOIN families f ON f.location_geom = clst.fam_geom", nativeQuery = true)
    List<Object[]> clusterFamiliesWithin(Double distance);
}
