package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.PackageGift;
import org.akcjamis.webapp.repository.PackageGiftRepository;
import org.akcjamis.webapp.repository.search.PackageGiftSearchRepository;
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
 * REST controller for managing PackageGift.
 */
@RestController
@RequestMapping("/api")
public class PackageGiftResource {

    private final Logger log = LoggerFactory.getLogger(PackageGiftResource.class);
        
    @Inject
    private PackageGiftRepository packageGiftRepository;
    
    @Inject
    private PackageGiftSearchRepository packageGiftSearchRepository;
    
    /**
     * POST  /package-gifts : Create a new packageGift.
     *
     * @param packageGift the packageGift to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packageGift, or with status 400 (Bad Request) if the packageGift has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-gifts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageGift> createPackageGift(@RequestBody PackageGift packageGift) throws URISyntaxException {
        log.debug("REST request to save PackageGift : {}", packageGift);
        if (packageGift.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("packageGift", "idexists", "A new packageGift cannot already have an ID")).body(null);
        }
        PackageGift result = packageGiftRepository.save(packageGift);
        packageGiftSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/package-gifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("packageGift", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /package-gifts : Updates an existing packageGift.
     *
     * @param packageGift the packageGift to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packageGift,
     * or with status 400 (Bad Request) if the packageGift is not valid,
     * or with status 500 (Internal Server Error) if the packageGift couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-gifts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageGift> updatePackageGift(@RequestBody PackageGift packageGift) throws URISyntaxException {
        log.debug("REST request to update PackageGift : {}", packageGift);
        if (packageGift.getId() == null) {
            return createPackageGift(packageGift);
        }
        PackageGift result = packageGiftRepository.save(packageGift);
        packageGiftSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("packageGift", packageGift.getId().toString()))
            .body(result);
    }

    /**
     * GET  /package-gifts : get all the packageGifts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of packageGifts in body
     */
    @RequestMapping(value = "/package-gifts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageGift> getAllPackageGifts() {
        log.debug("REST request to get all PackageGifts");
        List<PackageGift> packageGifts = packageGiftRepository.findAll();
        return packageGifts;
    }

    /**
     * GET  /package-gifts/:id : get the "id" packageGift.
     *
     * @param id the id of the packageGift to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packageGift, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/package-gifts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageGift> getPackageGift(@PathVariable Long id) {
        log.debug("REST request to get PackageGift : {}", id);
        PackageGift packageGift = packageGiftRepository.findOne(id);
        return Optional.ofNullable(packageGift)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /package-gifts/:id : delete the "id" packageGift.
     *
     * @param id the id of the packageGift to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/package-gifts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePackageGift(@PathVariable Long id) {
        log.debug("REST request to delete PackageGift : {}", id);
        packageGiftRepository.delete(id);
        packageGiftSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("packageGift", id.toString())).build();
    }

    /**
     * SEARCH  /_search/package-gifts?query=:query : search for the packageGift corresponding
     * to the query.
     *
     * @param query the query of the packageGift search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/package-gifts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageGift> searchPackageGifts(@RequestParam String query) {
        log.debug("REST request to search PackageGifts for query {}", query);
        return StreamSupport
            .stream(packageGiftSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
