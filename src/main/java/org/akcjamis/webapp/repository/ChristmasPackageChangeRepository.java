package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackageChange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackageChange entity.
 */
public interface ChristmasPackageChangeRepository extends JpaRepository<ChristmasPackageChange,Long> {

    Page<ChristmasPackageChange> findByChristmasPackage_id(Long id, Pageable var1);

    ChristmasPackageChange findByIdAndChristmasPackage_id(Long packageId, Long id);
}
