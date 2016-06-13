package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.ChristmasPackageChange;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.ChristmasPackageChangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.inject.Inject;

/**
 * Service Implementation for managing ChristmasPackage.
 */
@Service
@Transactional
public class ChristmasPackageService {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageService.class);

    private ChristmasPackageRepository christmasPackageRepository;

    private ChristmasPackageNoteRepository christmasPackageNoteRepository;

    private ChristmasPackageChangeRepository christmasPackageChangeRepository;

    @Inject
    public ChristmasPackageService(ChristmasPackageRepository christmasPackageRepository,
                                    ChristmasPackageNoteRepository christmasPackageNoteRepository,
                                    ChristmasPackageChangeRepository christmasPackageChangeRepository) {
        this.christmasPackageRepository = christmasPackageRepository;
        this.christmasPackageNoteRepository = christmasPackageNoteRepository;
        this.christmasPackageChangeRepository = christmasPackageChangeRepository;
    }

    /**
     * Save a christmasPackage for given event.
     *
     * @param eventYear year of the event
     * @param christmasPackage the entity to save
     * @return the persisted entity
     */
    public ChristmasPackage savePackage(Short eventYear, ChristmasPackage christmasPackage) {
        log.debug("Request to save ChristmasPackage : {}", christmasPackage);
        Event event = new Event();
        event.setYear(eventYear);
        christmasPackage.setEvent(event);
        return christmasPackageRepository.save(christmasPackage);
    }

    /**
     *  Get all the christmasPackages of event.
     *
     *  @param eventYear year of event
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChristmasPackage> findAll(Short eventYear, Pageable pageable) {
        log.debug("Request to get all ChristmasPackages");
        return christmasPackageRepository.findByEvent_year(eventYear, pageable);
    }

    /**
     *  Get all the christmasPackages list of event.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChristmasPackage> getList(Short eventYear, Pageable pageable){
        log.debug("Request to get all ChristmasPackages");
        return christmasPackageRepository.getList(eventYear, pageable);
    }

    /**
     *  Get one christmasPackage by year and id.
     *
     *  @param eventYear year of event
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChristmasPackage findOne(Short eventYear, Long id) {
        log.debug("Request to get ChristmasPackage : {}", id);
        return christmasPackageRepository.findByEvent_yearAndId(eventYear, id);
    }

    /**
     *  Get one christmasPackage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChristmasPackage findOne(Long id) {
        log.debug("Request to get ChristmasPackage : {}", id);
        return christmasPackageRepository.findOne(id);
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
    }

    /**
     * Add note to selected family.
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

        return christmasPackageChangeRepository.save(packageChange);
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
    }



}
