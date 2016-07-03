package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackageChange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ChristmasPackageChange entity.
 */
public interface ChristmasPackageChangeRepository extends JpaRepository<ChristmasPackageChange, Integer> {

    Page<ChristmasPackageChange> findByChristmasPackage_id(Integer id, Pageable var1);

    ChristmasPackageChange findByIdAndChristmasPackage_id(Integer packageId, Integer id);
}
