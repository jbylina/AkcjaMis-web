package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.PackageNote;
import org.akcjamis.webapp.repository.PackageNoteRepository;
import org.akcjamis.webapp.repository.search.PackageNoteSearchRepository;
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
 * REST controller for managing PackageNote.
 */
@RestController
@RequestMapping("/api")
public class PackageNoteResource {

    private final Logger log = LoggerFactory.getLogger(PackageNoteResource.class);
        
    @Inject
    private PackageNoteRepository packageNoteRepository;
    
    @Inject
    private PackageNoteSearchRepository packageNoteSearchRepository;
    
    /**
     * POST  /package-notes : Create a new packageNote.
     *
     * @param packageNote the packageNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packageNote, or with status 400 (Bad Request) if the packageNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageNote> createPackageNote(@RequestBody PackageNote packageNote) throws URISyntaxException {
        log.debug("REST request to save PackageNote : {}", packageNote);
        if (packageNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("packageNote", "idexists", "A new packageNote cannot already have an ID")).body(null);
        }
        PackageNote result = packageNoteRepository.save(packageNote);
        packageNoteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/package-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("packageNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /package-notes : Updates an existing packageNote.
     *
     * @param packageNote the packageNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packageNote,
     * or with status 400 (Bad Request) if the packageNote is not valid,
     * or with status 500 (Internal Server Error) if the packageNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/package-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageNote> updatePackageNote(@RequestBody PackageNote packageNote) throws URISyntaxException {
        log.debug("REST request to update PackageNote : {}", packageNote);
        if (packageNote.getId() == null) {
            return createPackageNote(packageNote);
        }
        PackageNote result = packageNoteRepository.save(packageNote);
        packageNoteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("packageNote", packageNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /package-notes : get all the packageNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of packageNotes in body
     */
    @RequestMapping(value = "/package-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageNote> getAllPackageNotes() {
        log.debug("REST request to get all PackageNotes");
        List<PackageNote> packageNotes = packageNoteRepository.findAll();
        return packageNotes;
    }

    /**
     * GET  /package-notes/:id : get the "id" packageNote.
     *
     * @param id the id of the packageNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packageNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/package-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PackageNote> getPackageNote(@PathVariable Long id) {
        log.debug("REST request to get PackageNote : {}", id);
        PackageNote packageNote = packageNoteRepository.findOne(id);
        return Optional.ofNullable(packageNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /package-notes/:id : delete the "id" packageNote.
     *
     * @param id the id of the packageNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/package-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePackageNote(@PathVariable Long id) {
        log.debug("REST request to delete PackageNote : {}", id);
        packageNoteRepository.delete(id);
        packageNoteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("packageNote", id.toString())).build();
    }

    /**
     * SEARCH  /_search/package-notes?query=:query : search for the packageNote corresponding
     * to the query.
     *
     * @param query the query of the packageNote search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/package-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PackageNote> searchPackageNotes(@RequestParam String query) {
        log.debug("REST request to search PackageNotes for query {}", query);
        return StreamSupport
            .stream(packageNoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
