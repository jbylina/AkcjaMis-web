package org.akcjamis.webapp.repository;

import com.vividsolutions.jts.geom.Geometry;
import org.akcjamis.webapp.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family,Long> {

    Family findByLocationGeom(Geometry geometry);
}
