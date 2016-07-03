package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackageChange;
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

/**
 * REST controller for managing ChristmasPackageChange.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageChangeResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageChangeResource.class);

    private  ChristmasPackageService christmasPackageService;

    @Inject
    public ChristmasPackageChangeResource(ChristmasPackageService christmasPackageService) {
        this.christmasPackageService = christmasPackageService;
    }

    /**
     * POST  /christmas-package/:id/changes : Create a new christmasPackageChange for selected christmasPackage.
     *
     * @param id the id of christmasPackage
     * @param christmasPackageChange the christmasPackageChange to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackageChange, or with status 400 (Bad Request) if the christmasPackageChange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package/{id}/changes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> createChristmasPackageChange(@PathVariable Integer id, @Valid @RequestBody ChristmasPackageChange christmasPackageChange) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackageChange : {}", christmasPackageChange);
        if (christmasPackageChange.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackageChange", "idexists", "A new christmasPackageChange cannot already have an ID")).body(null);
        }
        ChristmasPackageChange result = christmasPackageService.saveChange(id, christmasPackageChange);
        return ResponseEntity.created(new URI("/api/christmas-package/" + id + "/changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackageChange", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /christmas-package/:id/changes : Updates an existing christmasPackageChange.
     *
     * @param id the id of christmasPackage
     * @param christmasPackageChange the christmasPackageChange to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackageChange,
     * or with status 400 (Bad Request) if the christmasPackageChange is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackageChange couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package/{id}/changes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> updateChristmasPackageChange(@PathVariable Integer id, @Valid @RequestBody ChristmasPackageChange christmasPackageChange) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackageChange : {}", christmasPackageChange);
        if (christmasPackageChange.getId() == null) {
            return createChristmasPackageChange(id, christmasPackageChange);
        }
        ChristmasPackageChange result = christmasPackageService.saveChange(id, christmasPackageChange);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackageChange", christmasPackageChange.getId().toString()))
            .body(result);
    }

    /**
     * GET  /christmas-package/:id/changes : get all the christmasPackageChanges for selected christmasPackage.
     *
     * @param id the id of christmasPackage
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackageChanges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/christmas-package/{id}/changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackageChange>> getAllChristmasPackageChanges(@PathVariable Integer id, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChristmasPackageChanges");
        Page<ChristmasPackageChange> page = christmasPackageService.findAllChanges(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/christmas-package/" + id + "/changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /christmas-package/:packageId/changes/:id : get the "id" christmasPackageChange.
     *
     * @param packageId the id of christmasPackage
     * @param id the id of the christmasPackageChange to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackageChange, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/christmas-package/{packageId}/changes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageChange> getChristmasPackageChange(@PathVariable Integer packageId, @PathVariable Integer id) {
        log.debug("REST request to get ChristmasPackageChange : {}", id);
        ChristmasPackageChange christmasPackageChange = christmasPackageService.findOneChange(packageId, id);
        return Optional.ofNullable(christmasPackageChange)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /christmas-package/:packageId/changes/:id : delete the "id" christmasPackageChange.
     *
     * @param packageId the id of christmasPackage
     * @param id the id of the christmasPackageChange to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/christmas-package/{packageId}/changes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackageChange(@PathVariable Long packageId, @PathVariable Integer id) {
        log.debug("REST request to delete ChristmasPackageChange : {}", id);
        christmasPackageService.deleteChange(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackageChange", id.toString())).build();
    }
}
