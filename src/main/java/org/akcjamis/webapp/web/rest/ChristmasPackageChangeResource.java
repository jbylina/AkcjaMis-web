package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackageChange;
import org.akcjamis.webapp.repository.ChristmasPackageChangeRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageChangeSearchRepository;
import org.akcjamis.webapp.web.rest.util.HeaderUtil;
import org.akcjamis.webapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ChristmasPackageChange.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageChangeResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageChangeResource.class);
        
    @Inject
    private ChristmasPackageChangeRepository christmasPackageChangeRepository;
    
    @Inject
    private ChristmasPackageChangeSearchRepository christmasPackageChangeSearchRepository;
    
    /**
     * POST  /christmas-package-changes : Create a new christmasPackageChange.
     *
     * @param christmasPackageChange the christmasPackageChange to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackageChange, or with status 400 (Bad Request) if the christmasPackageChange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package-changes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> createChristmasPackageChange(@Valid @RequestBody ChristmasPackageChange christmasPackageChange) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackageChange : {}", christmasPackageChange);
        if (christmasPackageChange.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackageChange", "idexists", "A new christmasPackageChange cannot already have an ID")).body(null);
        }
        ChristmasPackageChange result = christmasPackageChangeRepository.save(christmasPackageChange);
        christmasPackageChangeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/christmas-package-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackageChange", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /christmas-package-changes : Updates an existing christmasPackageChange.
     *
     * @param christmasPackageChange the christmasPackageChange to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackageChange,
     * or with status 400 (Bad Request) if the christmasPackageChange is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackageChange couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package-changes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> updateChristmasPackageChange(@Valid @RequestBody ChristmasPackageChange christmasPackageChange) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackageChange : {}", christmasPackageChange);
        if (christmasPackageChange.getId() == null) {
            return createChristmasPackageChange(christmasPackageChange);
        }
        ChristmasPackageChange result = christmasPackageChangeRepository.save(christmasPackageChange);
        christmasPackageChangeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackageChange", christmasPackageChange.getId().toString()))
            .body(result);
    }

    /**
     * GET  /christmas-package-changes : get all the christmasPackageChanges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackageChanges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/christmas-package-changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackageChange>> getAllChristmasPackageChanges(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChristmasPackageChanges");
        Page<ChristmasPackageChange> page = christmasPackageChangeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/christmas-package-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /christmas-package-changes/:id : get the "id" christmasPackageChange.
     *
     * @param id the id of the christmasPackageChange to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackageChange, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/christmas-package-changes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> getChristmasPackageChange(@PathVariable Long id) {
        log.debug("REST request to get ChristmasPackageChange : {}", id);
        ChristmasPackageChange christmasPackageChange = christmasPackageChangeRepository.findOne(id);
        return Optional.ofNullable(christmasPackageChange)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /christmas-package-changes/:id : delete the "id" christmasPackageChange.
     *
     * @param id the id of the christmasPackageChange to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/christmas-package-changes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackageChange(@PathVariable Long id) {
        log.debug("REST request to delete ChristmasPackageChange : {}", id);
        christmasPackageChangeRepository.delete(id);
        christmasPackageChangeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackageChange", id.toString())).build();
    }

    /**
     * SEARCH  /_search/christmas-package-changes?query=:query : search for the christmasPackageChange corresponding
     * to the query.
     *
     * @param query the query of the christmasPackageChange search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/christmas-package-changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackageChange>> searchChristmasPackageChanges(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChristmasPackageChanges for query {}", query);
        Page<ChristmasPackageChange> page = christmasPackageChangeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/christmas-package-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
