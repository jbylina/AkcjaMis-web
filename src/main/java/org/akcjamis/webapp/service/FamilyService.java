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
        Family result = familyRepository.save(family);
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

    @Transactional(readOnly = true)
    public List<ClusteringResultDTO> clusterFamiliesWithin(Short eventYear, Double distance) {
        log.debug("Request to get clusterFamiliesWithin : {}", distance);
        List<Object[]> result = familyRepository.clusterFamiliesWithin(eventYear, distance);

        return result.stream()
            .map(o -> new ClusteringResultDTO(((BigInteger)o[0]).intValue(),
                (Integer)o[1],
                (String)o[2],
                (String)o[3],
                (String)o[4],
                (String)o[5],
                (String)o[6],
                (String)o[7])).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RouteDTO calculateOptimalRoute(Set<Integer> families, Double latitude, Double longitude) {

        List<Object[]> list = familyRepository.calculateOptimalRoute(families, latitude, longitude);

        //add start point - warehouse facility
        families.add(0);
        int[] fam = families.stream().sorted().mapToInt(Integer::intValue).toArray();
        double[][] distMatrix = new double[fam.length][fam.length];
        Table<Integer, Integer, String> routes = HashBasedTable.create();

        for (Object[] o : list) {
            int from = Ints.indexOf(fam, (Integer) o[0]);
            int to = Ints.indexOf(fam, (Integer) o[1]);
            distMatrix[from][to] = distMatrix[to][from] = (Double)o[2];
            routes.put(from, to, (String)o[3]);
            routes.put(to, from, (String)o[3]);
        }

        List<Object[]> optOrder = familyRepository.calculateOptimalOrder(ArrayUtils.toString(distMatrix));

        // starting point is also a last point
        optOrder.add(optOrder.get(0));

        List<Integer> orderedFamIds  = optOrder.stream().map(o -> fam[(int)o[1]]).collect(Collectors.toList());

        List<String> routePaths = Lists.newLinkedList();
        for (int i = 0; i < optOrder.size() - 1; i++) {
            routePaths.add(routes.get(optOrder.get(i)[1], optOrder.get(i + 1)[1]));
        }

        return new RouteDTO(orderedFamIds, routePaths);
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
