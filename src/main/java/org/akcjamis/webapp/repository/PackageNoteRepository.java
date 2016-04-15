package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.PackageNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PackageNote entity.
 */
public interface PackageNoteRepository extends JpaRepository<PackageNote,Long> {

}
