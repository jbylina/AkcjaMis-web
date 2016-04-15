package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.SubpackageNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SubpackageNote entity.
 */
public interface SubpackageNoteRepository extends JpaRepository<SubpackageNote,Long> {

}
