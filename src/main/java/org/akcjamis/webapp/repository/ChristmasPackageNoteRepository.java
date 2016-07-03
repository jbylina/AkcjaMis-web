package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the ChristmasPackageNote entity.
 */
public interface ChristmasPackageNoteRepository extends JpaRepository<ChristmasPackageNote, Integer> {

    List<ChristmasPackageNote> findByChristmasPackage_id(Integer id);

    ChristmasPackageNote findByIdAndChristmasPackage_id(Integer id, Integer packageId);

}
