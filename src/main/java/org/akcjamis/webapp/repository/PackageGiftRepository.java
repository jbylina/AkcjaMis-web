package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.PackageGift;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PackageGift entity.
 */
public interface PackageGiftRepository extends JpaRepository<PackageGift,Long> {

}
