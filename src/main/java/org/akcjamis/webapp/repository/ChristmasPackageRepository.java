package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ChristmasPackage entity.
 */
public interface ChristmasPackageRepository extends JpaRepository<ChristmasPackage,Long> {

    Page<ChristmasPackage> findByEvent_year(Short year, Pageable var1);

    ChristmasPackage findByEvent_yearAndId(Short eventYear, Long id);

    ChristmasPackage findById(Long id);
}
