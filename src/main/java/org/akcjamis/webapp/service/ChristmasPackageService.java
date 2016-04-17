package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ChristmasPackage.
 */
@Service
@Transactional
public class ChristmasPackageService {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageService.class);
    
    @Inject
    private ChristmasPackageRepository christmasPackageRepository;
    
    @Inject
    private ChristmasPackageSearchRepository christmasPackageSearchRepository;
    
    /**
     * Save a christmasPackage.
     * 
     * @param christmasPackage the entity to save
     * @return the persisted entity
     */
    public ChristmasPackage save(ChristmasPackage christmasPackage) {
        log.debug("Request to save ChristmasPackage : {}", christmasPackage);
        ChristmasPackage result = christmasPackageRepository.save(christmasPackage);
        christmasPackageSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the christmasPackages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChristmasPackage> findAll(Pageable pageable) {
        log.debug("Request to get all ChristmasPackages");
        Page<ChristmasPackage> result = christmasPackageRepository.findAll(pageable); 
        return result;
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
        ChristmasPackage christmasPackage = christmasPackageRepository.findOne(id);
        return christmasPackage;
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
}
