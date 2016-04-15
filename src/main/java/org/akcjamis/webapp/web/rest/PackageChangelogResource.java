package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.PackageChangelog;
import org.akcjamis.webapp.repository.PackageChangelogRepository;
import org.akcjamis.webapp.repository.search.PackageChangelogSearchRepository;
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
 * REST controller for managing PackageChangelog.
 */
@RestController
@RequestMapping("/api")
public class PackageChangelogResource {

    private final Logger log = LoggerFactory.getLogger(PackageChangelogResource.class);
        
    @Inject
    private PackageChangelogRepository packageChangelogRepository;
    
    @Inject
    private PackageChangelogSearchRepository packageChangelogSearchRepository;
    
    /**
     * POST  /package-changelogs : Create a new packageChangelog.
     *
     * @param packageChangelog the packageChangelog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packageChangelog, or with status 400 (Bad Request) if the packageChangelog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-changelogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageChangelog> createPackageChangelog(@RequestBody PackageChangelog packageChangelog) throws URISyntaxException {
        log.debug("REST request to save PackageChangelog : {}", packageChangelog);
        if (packageChangelog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("packageChangelog", "idexists", "A new packageChangelog cannot already have an ID")).body(null);
        }
        PackageChangelog result = packageChangelogRepository.save(packageChangelog);
        packageChangelogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/package-changelogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("packageChangelog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /package-changelogs : Updates an existing packageChangelog.
     *
     * @param packageChangelog the packageChangelog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packageChangelog,
     * or with status 400 (Bad Request) if the packageChangelog is not valid,
     * or with status 500 (Internal Server Error) if the packageChangelog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-changelogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageChangelog> updatePackageChangelog(@RequestBody PackageChangelog packageChangelog) throws URISyntaxException {
        log.debug("REST request to update PackageChangelog : {}", packageChangelog);
        if (packageChangelog.getId() == null) {
            return createPackageChangelog(packageChangelog);
        }
        PackageChangelog result = packageChangelogRepository.save(packageChangelog);
        packageChangelogSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("packageChangelog", packageChangelog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /package-changelogs : get all the packageChangelogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of packageChangelogs in body
     */
    @RequestMapping(value = "/package-changelogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageChangelog> getAllPackageChangelogs() {
        log.debug("REST request to get all PackageChangelogs");
        List<PackageChangelog> packageChangelogs = packageChangelogRepository.findAll();
        return packageChangelogs;
    }

    /**
     * GET  /package-changelogs/:id : get the "id" packageChangelog.
     *
     * @param id the id of the packageChangelog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packageChangelog, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/package-changelogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageChangelog> getPackageChangelog(@PathVariable Long id) {
        log.debug("REST request to get PackageChangelog : {}", id);
        PackageChangelog packageChangelog = packageChangelogRepository.findOne(id);
        return Optional.ofNullable(packageChangelog)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /package-changelogs/:id : delete the "id" packageChangelog.
     *
     * @param id the id of the packageChangelog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/package-changelogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePackageChangelog(@PathVariable Long id) {
        log.debug("REST request to delete PackageChangelog : {}", id);
        packageChangelogRepository.delete(id);
        packageChangelogSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("packageChangelog", id.toString())).build();
    }

    /**
     * SEARCH  /_search/package-changelogs?query=:query : search for the packageChangelog corresponding
     * to the query.
     *
     * @param query the query of the packageChangelog search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/package-changelogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageChangelog> searchPackageChangelogs(@RequestParam String query) {
        log.debug("REST request to search PackageChangelogs for query {}", query);
        return StreamSupport
            .stream(packageChangelogSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
