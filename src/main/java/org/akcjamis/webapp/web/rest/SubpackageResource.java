package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Subpackage;
import org.akcjamis.webapp.repository.SubpackageRepository;
import org.akcjamis.webapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Subpackage.
 */
@RestController
@RequestMapping("/api")
public class SubpackageResource {

    private final Logger log = LoggerFactory.getLogger(SubpackageResource.class);

    @Inject
    private SubpackageRepository subpackageRepository;

    /**
     * POST  /subpackages : Create a new subpackage.
     *
     * @param subpackage the subpackage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subpackage, or with status 400 (Bad Request) if the subpackage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subpackages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<Subpackage> createSubpackage(@Valid @RequestBody Subpackage subpackage) throws URISyntaxException {
        log.debug("REST request to save Subpackage : {}", subpackage);
        if (subpackage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subpackage", "idexists", "A new subpackage cannot already have an ID")).body(null);
        }
        Subpackage result = subpackageRepository.save(subpackage);
        return ResponseEntity.created(new URI("/api/subpackages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subpackage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subpackages : Updates an existing subpackage.
     *
     * @param subpackage the subpackage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subpackage,
     * or with status 400 (Bad Request) if the subpackage is not valid,
     * or with status 500 (Internal Server Error) if the subpackage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subpackages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<Subpackage> updateSubpackage(@Valid @RequestBody Subpackage subpackage) throws URISyntaxException {
        log.debug("REST request to update Subpackage : {}", subpackage);
        if (subpackage.getId() == null) {
            return createSubpackage(subpackage);
        }
        Subpackage result = subpackageRepository.save(subpackage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subpackage", subpackage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subpackages : get all the subpackages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subpackages in body
     */
    @RequestMapping(value = "/subpackages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public List<Subpackage> getAllSubpackages() {
        log.debug("REST request to get all Subpackages");
        List<Subpackage> subpackages = subpackageRepository.findAll();
        return subpackages;
    }

    /**
     * GET  /subpackages/:id : get the "id" subpackage.
     *
     * @param id the id of the subpackage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subpackage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subpackages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<Subpackage> getSubpackage(@PathVariable Integer id) {
        log.debug("REST request to get Subpackage : {}", id);
        Subpackage subpackage = subpackageRepository.findOne(id);
        return Optional.ofNullable(subpackage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subpackages/:id : delete the "id" subpackage.
     *
     * @param id the id of the subpackage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subpackages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubpackage(@PathVariable Integer id) {
        log.debug("REST request to delete Subpackage : {}", id);
        subpackageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subpackage", id.toString())).build();
    }
}
