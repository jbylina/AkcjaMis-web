package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackage entity.
 */
public interface ChristmasPackageRepository extends JpaRepository<ChristmasPackage,Long> {

    Page<ChristmasPackage> findByEvent_id(Long id, Pageable var1);

    ChristmasPackage findByIdAndEvent_id(Long eventId, Long id);

    ChristmasPackage findById(Long id);
}
