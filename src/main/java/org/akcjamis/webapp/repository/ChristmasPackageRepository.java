package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackage entity.
 */
public interface ChristmasPackageRepository extends JpaRepository<ChristmasPackage,Long> {

}
