package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.repository.FamilyNoteRepository;
import org.akcjamis.webapp.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing FamilyNote.
 */
@Service
@Transactional
public class FamilyNoteService {

    private final Logger log = LoggerFactory.getLogger(FamilyNoteService.class);

    private FamilyNoteRepository familyNoteRepository;

    private TagRepository tagRepository;

    @Inject
    public FamilyNoteService(FamilyNoteRepository familyNoteRepository, TagRepository tagRepository) {
        this.familyNoteRepository = familyNoteRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Save a familyNote for given family id.
     *
     * @param familyId family id
     * @param familyNote the note to save
     * @return the persisted entity
     */
    public FamilyNote save(Integer familyId, FamilyNote familyNote) {
        log.debug("Request to save FamilyNote : {}", familyNote);
        Family family = new Family();
        family.setId(familyId);
        familyNote.setFamily(family);
        FamilyNote result = familyNoteRepository.save(familyNote);
        return result;
    }

    /**
     *  Get all the familyNotes of family.
     *
     *  @param familyId family id
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FamilyNote> findAll(Integer familyId, Pageable pageable) {
        log.debug("Request to get all FamilyNotes");
        return familyNoteRepository.findByFamily_id(familyId, pageable);
    }

    /**
     *  Get one familyNote by id of family.
     *
     *  @param familyId the id of family
     *  @param noteId the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FamilyNote findOne(Integer familyId, Integer noteId) {
        log.debug("Request to get FamilyNote : {}", noteId);
        return familyNoteRepository.findOneWithEagerRelationships(familyId, noteId);
    }

    /**
     *  Delete the  familyNote by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete FamilyNote : {}", id);
        familyNoteRepository.delete(id);
    }
}
