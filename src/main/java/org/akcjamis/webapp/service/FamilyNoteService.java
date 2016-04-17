package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.repository.FamilyNoteRepository;
import org.akcjamis.webapp.repository.search.FamilyNoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FamilyNote.
 */
@Service
@Transactional
public class FamilyNoteService {

    private final Logger log = LoggerFactory.getLogger(FamilyNoteService.class);

    @Inject
    private FamilyNoteRepository familyNoteRepository;

    @Inject
    private FamilyNoteSearchRepository familyNoteSearchRepository;

    /**
     * Save a familyNote for given family id.
     *
     * @param familyId family id
     * @param familyNote the entity to save
     * @return the persisted entity
     */
    public FamilyNote save(Long familyId, FamilyNote familyNote) {
        log.debug("Request to save FamilyNote : {}", familyNote);
        Family family = new Family();
        family.setId(familyId);
        familyNote.setFamily(family);
        FamilyNote result = familyNoteRepository.save(familyNote);
        familyNoteSearchRepository.save(result);
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
    public Page<FamilyNote> findAll(Long familyId, Pageable pageable) {
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
    public FamilyNote findOne(Long noteId) {
        log.debug("Request to get FamilyNote : {}", noteId);
        return familyNoteRepository.findOneWithEagerRelationships(noteId);
    }

    /**
     *  Delete the  familyNote by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FamilyNote : {}", id);
        familyNoteRepository.delete(id);
        familyNoteSearchRepository.delete(id);
    }

    /**
     * Search for the familyNote corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FamilyNote> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FamilyNotes for query {}", query);
        return familyNoteSearchRepository.search(queryStringQuery(query), pageable);
    }
}
