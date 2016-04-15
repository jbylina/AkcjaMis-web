package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Sybpackage;
import org.akcjamis.webapp.repository.SybpackageRepository;
import org.akcjamis.webapp.repository.search.SybpackageSearchRepository;
import org.akcjamis.webapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Sybpackage.
 */
@RestController
@RequestMapping("/api")
public class SybpackageResource {

    private final Logger log = LoggerFactory.getLogger(SybpackageResource.class);
        
    @Inject
    private SybpackageRepository sybpackageRepository;
    
    @Inject
    private SybpackageSearchRepository sybpackageSearchRepository;
    
    /**
     * POST  /sybpackages : Create a new sybpackage.
     *
     * @param sybpackage the sybpackage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sybpackage, or with status 400 (Bad Request) if the sybpackage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sybpackages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sybpackage> createSybpackage(@RequestBody Sybpackage sybpackage) throws URISyntaxException {
        log.debug("REST request to save Sybpackage : {}", sybpackage);
        if (sybpackage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sybpackage", "idexists", "A new sybpackage cannot already have an ID")).body(null);
        }
        Sybpackage result = sybpackageRepository.save(sybpackage);
        sybpackageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sybpackages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sybpackage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sybpackages : Updates an existing sybpackage.
     *
     * @param sybpackage the sybpackage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sybpackage,
     * or with status 400 (Bad Request) if the sybpackage is not valid,
     * or with status 500 (Internal Server Error) if the sybpackage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sybpackages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sybpackage> updateSybpackage(@RequestBody Sybpackage sybpackage) throws URISyntaxException {
        log.debug("REST request to update Sybpackage : {}", sybpackage);
        if (sybpackage.getId() == null) {
            return createSybpackage(sybpackage);
        }
        Sybpackage result = sybpackageRepository.save(sybpackage);
        sybpackageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sybpackage", sybpackage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sybpackages : get all the sybpackages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sybpackages in body
     */
    @RequestMapping(value = "/sybpackages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sybpackage> getAllSybpackages() {
        log.debug("REST request to get all Sybpackages");
        List<Sybpackage> sybpackages = sybpackageRepository.findAll();
        return sybpackages;
    }

    /**
     * GET  /sybpackages/:id : get the "id" sybpackage.
     *
     * @param id the id of the sybpackage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sybpackage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sybpackages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sybpackage> getSybpackage(@PathVariable Long id) {
        log.debug("REST request to get Sybpackage : {}", id);
        Sybpackage sybpackage = sybpackageRepository.findOne(id);
        return Optional.ofNullable(sybpackage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sybpackages/:id : delete the "id" sybpackage.
     *
     * @param id the id of the sybpackage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sybpackages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSybpackage(@PathVariable Long id) {
        log.debug("REST request to delete Sybpackage : {}", id);
        sybpackageRepository.delete(id);
        sybpackageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sybpackage", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sybpackages?query=:query : search for the sybpackage corresponding
     * to the query.
     *
     * @param query the query of the sybpackage search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/sybpackages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sybpackage> searchSybpackages(@RequestParam String query) {
        log.debug("REST request to search Sybpackages for query {}", query);
        return StreamSupport
            .stream(sybpackageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
