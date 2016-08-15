package org.akcjamis.webapp.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;
import org.akcjamis.webapp.domain.*;
import org.akcjamis.webapp.repository.*;
import org.akcjamis.webapp.web.rest.dto.ClusteringResultDTO;
import org.akcjamis.webapp.web.rest.dto.RouteDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Family.
 */
@Service
@Transactional
public class FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private FamilyRepository familyRepository;

    private ContactRepository contactRepository;

    private ChildRepository childRepository;

    private ChristmasPackageRepository christmasPackageRepository;

    private EventRepository eventRepository;

    private SubpackageRepository subpackageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public FamilyService(FamilyRepository familyRepository,
                         ContactRepository contactRepository,
                         ChildRepository childRepository,
                         ChristmasPackageRepository christmasPackageRepository,
                         EventRepository eventRepository,
                         SubpackageRepository subpackageRepository) {
        this.familyRepository = familyRepository;
        this.contactRepository = contactRepository;
        this.childRepository = childRepository;
        this.christmasPackageRepository = christmasPackageRepository;
        this.eventRepository = eventRepository;
        this.subpackageRepository = subpackageRepository;
    }

    /**
     * Save a family.
     *
     * @param family the entity to save
     * @return the persisted entity
     */
    public Family save(Family family) {
        log.debug("Request to save Family : {}", family);
        Family result = familyRepository.saveAndFlush(family);
        return familyRepository.findOne(result.getId());
    }

    /**
     * Update a family.
     *
     * @param family the entity to save
     * @return the persisted entity
     */
    public Family update(Family family) {
        log.debug("Request to update Family : {}", family);
        Family result = entityManager.merge(family);
        entityManager.refresh(result);
        return result;
    }

    /**
     *  Get all the families.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Family> findAll(Pageable pageable) {
        log.debug("Request to get all Families");
        return familyRepository.findAll(pageable);
    }

    /**
     *  Get one family by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Family findOne(Integer id) {
        log.debug("Request to get Family : {}", id);
        return familyRepository.findOne(id);
    }

    /**
     *  Delete the  family by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.delete(id);
    }

    /**
     * Add contact to selected family.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    public Contact saveContact(Integer id, Contact contact) {
        log.debug("Request to save Contact : {}", contact);
        Family family = new Family();
        family.setId(id);
        contact.setFamily(family);
        Contact result = contactRepository.save(contact);
        return result;
    }

    /**
     * Get all contacts of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<Contact> getAllContacts(Integer id) {
        log.debug("Request contacts for Family : {}", id);
        return contactRepository.findByFamily_id(id);
    }

    /**
     * Add child to selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public Child saveChild(Integer id, Child child) {
        log.debug("Request to save Child : {}", child);
        Family family = new Family();
        family.setId(id);
        child.setFamily(family);
        Child result = childRepository.save(child);
        return result;
    }

    /**
     * Get all children of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<Child> getAllChildren(Integer id) {
        log.debug("Request children for Family : {}", id);
        return childRepository.findByFamily_id(id);
    }

    /**
     * Get all packages of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<ChristmasPackage> getChristmasPackages(Integer id) {
        log.debug("Request packages for Family : {}", id);
        return christmasPackageRepository.findByFamily_id(id);
    }

    /**
     * Add family to event
     *
     * @param familyId the id of family
     * @return the persisted entity
     */
    public ChristmasPackage addFamilyToEvent(Integer familyId) {
        log.debug("Request add Family to event : {}", familyId);
        // TODO reorganize

        Family family = familyRepository.findOne(familyId);
        Event latestEvent = eventRepository.findTop1ByOrderByYearDesc();
        ChristmasPackage pkg = christmasPackageRepository.findByFamily_idAndEvent_year(familyId, latestEvent.getYear());
        if(pkg == null){
            ChristmasPackage christmasPackage = new ChristmasPackage();
            christmasPackage.setEvent(latestEvent);
            christmasPackage.setFamily(family);
            christmasPackage = christmasPackageRepository.save(christmasPackage);

            int number = 1;
            for (Child c : family.getChilds()) {
                Subpackage subpackage = new Subpackage();
                subpackage.setChild(c);
                subpackage.setChristmasPackage(christmasPackage);
                subpackage.setSubpackageNumber(number);
                subpackage = subpackageRepository.save(subpackage);
                number++;
            }

            pkg = christmasPackageRepository.findByFamily_idAndEvent_year(familyId, latestEvent.getYear());
        }

        return pkg;
    }
}
