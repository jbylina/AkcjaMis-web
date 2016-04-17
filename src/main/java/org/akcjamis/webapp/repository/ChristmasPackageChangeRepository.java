package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackageChange;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackageChange entity.
 */
public interface ChristmasPackageChangeRepository extends JpaRepository<ChristmasPackageChange,Long> {

}
