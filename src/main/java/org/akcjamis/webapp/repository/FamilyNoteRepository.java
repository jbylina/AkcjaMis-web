package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.FamilyNote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FamilyNote entity.
 */
public interface FamilyNoteRepository extends JpaRepository<FamilyNote, Integer> {

    @Query("select distinct familyNote from FamilyNote familyNote left join fetch familyNote.tags")
    List<FamilyNote> findAllWithEagerRelationships();

    @Query("select familyNote from FamilyNote familyNote left join fetch familyNote.tags where familyNote.id =:noteId and familyNote.family.id = :familyId")
    FamilyNote findOneWithEagerRelationships(@Param("familyId") Integer familyId, @Param("noteId") Integer noteId);

    Page<FamilyNote> findByFamily_id(Integer id, Pageable var1);
}
