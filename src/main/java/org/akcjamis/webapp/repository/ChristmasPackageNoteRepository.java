package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackageNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackageNote entity.
 */
public interface ChristmasPackageNoteRepository extends JpaRepository<ChristmasPackageNote,Long> {

    List<ChristmasPackageNote> findByChristmasPackage_id(Long id);

    ChristmasPackageNote findByIdAndChristmasPackage_id(Long id, Long packageId);

}
