package org.akcjamis.webapp.repository;

import com.vividsolutions.jts.geom.Geometry;
import org.akcjamis.webapp.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family,Long> {

    Family findByLocationGeom(Geometry geometry);

    @Query(value = "SELECT DISTINCT clst.clst_num as cluster_num, " +
                   "f.id," +
                   "f.street," +
                   "f.house_no," +
                   "f.flat_no," +
                   "f.postalcode," +
                   "f.city," +
                   "st_astext(f.location_geom) location_geom" +
                   "  FROM (SELECT row_number() OVER () clst_num" +
                   "              ,(st_DumpPoints(clst.geom)).geom fam_geom" +
                   "            FROM (SELECT unnest(ST_ClusterWithin(location_geom, :distance)) geom" +
                   "                  FROM families" +
                   "                  WHERE location_geom IS NOT NULL) clst) clst" +
                   " LEFT JOIN families f ON f.location_geom = clst.fam_geom", nativeQuery = true)
    List<Object[]> clusterFamiliesWithin(@Param("distance") Double distance);


    @Query(value = "SELECT seq, id FROM pgr_tsp(:distanceMatrix \\:\\: float8[],0)", nativeQuery = true)
    List<Object[]> calculateOptimalOrder(@Param("distanceMatrix") String distanceMatrix);

    @Query(value = "WITH fam_facility_w_nearest_node AS ( " +
                   "  SELECT " +
                   "      f.id, " +
                   "      (SELECT w.source " +
                   "       FROM osm_ways w " +
                   "       ORDER BY w.the_geom <-> f.location_geom " +
                   "       LIMIT 1) node_num " +
                   "    FROM families f " +
                   "    WHERE f.location_geom IS NOT NULL " +
                   "          AND f.id IN (:families) " +
                   "UNION ALL " +
                   "  SELECT 0 id, " +
                   "    (SELECT w.source " +
                   "     FROM osm_ways w " +
                   "     ORDER BY w.the_geom  <-> st_pointfromtext('POINT(' || :longitude || ' ' || :latitude || ')', 4326) " +
                   "     LIMIT 1) node_num " +
                   ") " +
                   "SELECT " +
                   "  comb.start_n_f_id, " +
                   "  comb.end_n_f_id, " +
                   "  SUM(r.cost) \"cost\", " +
                   "  st_astext(ST_Linemerge(ST_Union(w.the_geom ORDER BY r.seq))) route " +
                   "  FROM (SELECT " +
                   "        a.id       start_n_f_id, " +
                   "        a.node_num start_node, " +
                   "        b.id       end_n_f_id, " +
                   "        b.node_num end_node " +
                   "      FROM fam_facility_w_nearest_node a, fam_facility_w_nearest_node b " +
                   "      WHERE a.node_num <> b.node_num " +
                   "            AND a.node_num < b.node_num " +
                   "     ) comb, " +
                   "  pgr_astar( " +
                   "      'SELECT gid AS id, source::int4, target::int4, x1, y1, x2, y2, cost_s::float8 AS cost, reverse_cost_s as reverse_cost FROM osm_ways', " +
                   "      comb.start_node \\:\\: INT4, comb.end_node \\:\\: INT4, TRUE, TRUE) r " +
                   "  LEFT JOIN osm_ways w ON w.gid = r.id2 " +
                   " GROUP BY comb.start_n_f_id, comb.end_n_f_id ", nativeQuery = true)
    List<Object[]> calculateOptimalRoute(@Param("families") Set<Long> families, @Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
