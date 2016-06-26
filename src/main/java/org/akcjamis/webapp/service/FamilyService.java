package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.Child;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.Contact;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.repository.ChildRepository;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.ContactRepository;
import org.akcjamis.webapp.repository.FamilyRepository;
import org.akcjamis.webapp.repository.search.ChildSearchRepository;
import org.akcjamis.webapp.repository.search.ContactSearchRepository;
import org.akcjamis.webapp.repository.search.FamilySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Family.
 */
@Service
@Transactional
public class FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private FamilyRepository familyRepository;

    private FamilySearchRepository familySearchRepository;

    private ContactRepository contactRepository;

    private ContactSearchRepository contactSearchRepository;

    private ChildRepository childRepository;

    private ChildSearchRepository childSearchRepository;

    private ChristmasPackageRepository christmasPackageRepository;

    @Inject
    public FamilyService(FamilyRepository familyRepository,
                         FamilySearchRepository familySearchRepository,
                         ContactRepository contactRepository,
                         ContactSearchRepository contactSearchRepository,
                         ChildRepository childRepository,
                         ChildSearchRepository childSearchRepository,
                         ChristmasPackageRepository christmasPackageRepository) {
        this.familyRepository = familyRepository;
        this.familySearchRepository = familySearchRepository;
        this.contactRepository = contactRepository;
        this.contactSearchRepository = contactSearchRepository;
        this.childRepository = childRepository;
        this.childSearchRepository = childSearchRepository;
        this.christmasPackageRepository = christmasPackageRepository;
    }

    /**
     * Save a family.
     *
     * @param family the entity to save
     * @return the persisted entity
     */
    public Family save(Family family) {
        log.debug("Request to save Family : {}", family);
        Family result = familyRepository.save(family);
        familySearchRepository.save(result);
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
    public Family findOne(Long id) {
        log.debug("Request to get Family : {}", id);
        return familyRepository.findOne(id);
    }

    /**
     *  Delete the  family by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.delete(id);
        familySearchRepository.delete(id);
    }

    /**
     * Add contact to selected family.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    public Contact saveContact(Long id, Contact contact) {
        log.debug("Request to save Contact : {}", contact);
        Family family = new Family();
        family.setId(id);
        contact.setFamily(family);
        Contact result = contactRepository.save(contact);
        contactSearchRepository.save(result);
        return result;
    }

    /**
     * Get all contacts of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<Contact> getAllContacts(Long id) {
        log.debug("Request contacts for Family : {}", id);
        return contactRepository.findByFamily_id(id);
    }

    /**
     * Add child to selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public Child saveChild(Long id, Child child) {
        log.debug("Request to save Child : {}", child);
        Family family = new Family();
        family.setId(id);
        child.setFamily(family);
        Child result = childRepository.save(child);
        childSearchRepository.save(result);
        return result;
    }

    /**
     * Get all children of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<Child> getAllChildren(Long id) {
        log.debug("Request children for Family : {}", id);
        return childRepository.findByFamily_id(id);
    }

    /**
     * Get all packages of selected family.
     *
     * @param id the id of family
     * @return the persisted entity
     */
    public List<ChristmasPackage> getChristmasPackages(Long id) {
        log.debug("Request packages for Family : {}", id);
        return christmasPackageRepository.findByFamily_id(id);
    }

    /**
     * Search for the family corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Family> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Families for query {}", query);
        return familySearchRepository.search(queryStringQuery(query), pageable);
    }
}
