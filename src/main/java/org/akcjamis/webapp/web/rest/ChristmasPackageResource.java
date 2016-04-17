package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.service.ChristmasPackageService;
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
 * REST controller for managing ChristmasPackage.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageResource.class);
        
    @Inject
    private ChristmasPackageService christmasPackageService;
    
    /**
     * POST  /christmas-packages : Create a new christmasPackage.
     *
     * @param christmasPackage the christmasPackage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackage, or with status 400 (Bad Request) if the christmasPackage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-packages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackage> createChristmasPackage(@Valid @RequestBody ChristmasPackage christmasPackage) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackage : {}", christmasPackage);
        if (christmasPackage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackage", "idexists", "A new christmasPackage cannot already have an ID")).body(null);
        }
        ChristmasPackage result = christmasPackageService.save(christmasPackage);
        return ResponseEntity.created(new URI("/api/christmas-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /christmas-packages : Updates an existing christmasPackage.
     *
     * @param christmasPackage the christmasPackage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackage,
     * or with status 400 (Bad Request) if the christmasPackage is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-packages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackage> updateChristmasPackage(@Valid @RequestBody ChristmasPackage christmasPackage) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackage : {}", christmasPackage);
        if (christmasPackage.getId() == null) {
            return createChristmasPackage(christmasPackage);
        }
        ChristmasPackage result = christmasPackageService.save(christmasPackage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackage", christmasPackage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /christmas-packages : get all the christmasPackages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/christmas-packages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackage>> getAllChristmasPackages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChristmasPackages");
        Page<ChristmasPackage> page = christmasPackageService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/christmas-packages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /christmas-packages/:id : get the "id" christmasPackage.
     *
     * @param id the id of the christmasPackage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/christmas-packages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackage> getChristmasPackage(@PathVariable Long id) {
        log.debug("REST request to get ChristmasPackage : {}", id);
        ChristmasPackage christmasPackage = christmasPackageService.findOne(id);
        return Optional.ofNullable(christmasPackage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /christmas-packages/:id : delete the "id" christmasPackage.
     *
     * @param id the id of the christmasPackage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/christmas-packages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackage(@PathVariable Long id) {
        log.debug("REST request to delete ChristmasPackage : {}", id);
        christmasPackageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackage", id.toString())).build();
    }

    /**
     * SEARCH  /_search/christmas-packages?query=:query : search for the christmasPackage corresponding
     * to the query.
     *
     * @param query the query of the christmasPackage search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/christmas-packages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackage>> searchChristmasPackages(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChristmasPackages for query {}", query);
        Page<ChristmasPackage> page = christmasPackageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/christmas-packages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
