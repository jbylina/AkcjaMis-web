package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.ChristmasPackageChange;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageNoteSearchRepository;
import org.akcjamis.webapp.repository.ChristmasPackageChangeRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageSearchRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageChangeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ChristmasPackage.
 */
@Service
@Transactional
public class ChristmasPackageService {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageService.class);

    private ChristmasPackageRepository christmasPackageRepository;

    private ChristmasPackageSearchRepository christmasPackageSearchRepository;

    private ChristmasPackageNoteRepository christmasPackageNoteRepository;

    private ChristmasPackageNoteSearchRepository christmasPackageNoteSearchRepository;

    private ChristmasPackageChangeRepository christmasPackageChangeRepository;

    private ChristmasPackageChangeSearchRepository christmasPackageChangeSearchRepository;

    @Inject
    public ChristmasPackageService(ChristmasPackageRepository christmasPackageRepository,
                                   ChristmasPackageSearchRepository christmasPackageSearchRepository,
                                   ChristmasPackageNoteRepository christmasPackageNoteRepository,
                                   ChristmasPackageNoteSearchRepository christmasPackageNoteSearchRepository,
                                   ChristmasPackageChangeRepository christmasPackageChangeRepository,
                                   ChristmasPackageChangeSearchRepository christmasPackageChangeSearchRepository) {
        this.christmasPackageRepository = christmasPackageRepository;
        this.christmasPackageSearchRepository = christmasPackageSearchRepository;
        this.christmasPackageNoteRepository = christmasPackageNoteRepository;
        this.christmasPackageNoteSearchRepository = christmasPackageNoteSearchRepository;
        this.christmasPackageChangeRepository = christmasPackageChangeRepository;
        this.christmasPackageChangeSearchRepository = christmasPackageChangeSearchRepository;
    }

    /**
     * Save a christmasPackage for given event.
     *
     * @param eventId the eventId
     * @param christmasPackage the entity to save
     * @return the persisted entity
     */
    public ChristmasPackage save(Long eventId, ChristmasPackage christmasPackage) {
        log.debug("Request to save ChristmasPackage : {}", christmasPackage);
        Event event = new Event();
        event.setId(eventId);
        christmasPackage.setEvent(event);
        ChristmasPackage result = christmasPackageRepository.save(christmasPackage);
        christmasPackageSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the christmasPackages of event.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChristmasPackage> findAll(Long eventId, Pageable pageable) {
        log.debug("Request to get all ChristmasPackages");
        return christmasPackageRepository.findByEvent_id(eventId, pageable);
    }

    /**
     *  Get one christmasPackage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChristmasPackage findOne(Long eventId, Long id) {
        log.debug("Request to get ChristmasPackage : {}", id);
        return christmasPackageRepository.findByIdAndEvent_id(eventId, id);
    }

    /**
     *  Get one christmasPackage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChristmasPackage findOneById(Long id) {
        log.debug("Request to get ChristmasPackage : {}", id);
        return christmasPackageRepository.findById(id);
    }

    /**
     *  Delete the  christmasPackage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChristmasPackage : {}", id);
        christmasPackageRepository.delete(id);
        christmasPackageSearchRepository.delete(id);
    }

    /**
     * Search for the christmasPackage corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChristmasPackage> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChristmasPackages for query {}", query);
        return christmasPackageSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     * Add contact to selected family.
     *
     * @param id the id of christmas package
     * @param packageNote the entity to save
     * @return the persisted entity
     */
    public ChristmasPackageNote saveNote(Long id, ChristmasPackageNote packageNote) {
        log.debug("Request to save ChristmasPackageNote : {}", packageNote);

        ChristmasPackage christmasPackage = new ChristmasPackage();

        christmasPackage.setId(id);
        packageNote.setChristmasPackage(christmasPackage);

        ChristmasPackageNote result = christmasPackageNoteRepository.save(packageNote);
        christmasPackageNoteSearchRepository.save(result);

        return result;
    }

    /**
     * Get all package notes of selected package.
     *
     * @param id the id of christmas package
     * @return the persisted entity
     */

    public List<ChristmasPackageNote> getAllPackageNotes(Long id) {
        log.debug("Request contacts for ChristamasPackageNote : {}", id);
        return christmasPackageNoteRepository.findByChristmasPackage_id(id);

    }

    /**
     * Delete package note.
     *
     * @param id the id of christmas package
     */

    public void deletePackageNote(Long id) {
        log.debug("Request contacts for ChristamasPackageNote : {}", id);
        christmasPackageNoteRepository.delete(id);
        christmasPackageNoteSearchRepository.delete(id);
    }
    ;

    /**
     * Add change to selected christmas package
     *
     *  @param id the id of christmas package
     *  @param packageChange the entity to seve
     *  @return the list of entities
     */
    public ChristmasPackageChange saveChange(Long id, ChristmasPackageChange packageChange) {
        log.debug("Request to save ChristmasPackageChange : {}", packageChange);

        ChristmasPackage christmasPackage = new ChristmasPackage();

        christmasPackage.setId(id);
        packageChange.setChristmasPackage(christmasPackage);

        ChristmasPackageChange result = christmasPackageChangeRepository.save(packageChange);
        christmasPackageChangeSearchRepository.save(result);

        return result;
    }

    /**
     * Get all changes of selected christmas package
     *
     *  @param id the id of christmas package
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<ChristmasPackageChange> findAllChanges(Long id, Pageable pageable) {
        log.debug("Request Changes for ChristmasPackage : {}", id);

        return christmasPackageChangeRepository.findByChristmasPackage_id(id, pageable);
    }

    /**
     * Get one changes of selected christmas package
     *
     *  @param  packageId the id of christmas package
     *  @param id the id of change
     *  @return the list of entities
     */
    public ChristmasPackageChange findOneChange(Long packageId, Long id) {
        log.debug("Request Changes for ChristmasPackage : {}", id);

        return christmasPackageChangeRepository.findByIdAndChristmasPackage_id(packageId, id);
    }

    /**
     * Delete change
     *
     *  @param id the id of change
     *  @return the list of entities
     */
    public void deleteChange(Long id) {
        log.debug("Request delete Change : {}", id);

        christmasPackageChangeRepository.delete(id);
        christmasPackageChangeSearchRepository.delete(id);
    }



}
