package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.repository.FamilyNoteRepository;
import org.akcjamis.webapp.repository.search.FamilyNoteSearchRepository;
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
 * REST controller for managing FamilyNote.
 */
@RestController
@RequestMapping("/api")
public class FamilyNoteResource {

    private final Logger log = LoggerFactory.getLogger(FamilyNoteResource.class);
        
    @Inject
    private FamilyNoteRepository familyNoteRepository;
    
    @Inject
    private FamilyNoteSearchRepository familyNoteSearchRepository;
    
    /**
     * POST  /family-notes : Create a new familyNote.
     *
     * @param familyNote the familyNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familyNote, or with status 400 (Bad Request) if the familyNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/family-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> createFamilyNote(@RequestBody FamilyNote familyNote) throws URISyntaxException {
        log.debug("REST request to save FamilyNote : {}", familyNote);
        if (familyNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("familyNote", "idexists", "A new familyNote cannot already have an ID")).body(null);
        }
        FamilyNote result = familyNoteRepository.save(familyNote);
        familyNoteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/family-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("familyNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /family-notes : Updates an existing familyNote.
     *
     * @param familyNote the familyNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familyNote,
     * or with status 400 (Bad Request) if the familyNote is not valid,
     * or with status 500 (Internal Server Error) if the familyNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/family-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> updateFamilyNote(@RequestBody FamilyNote familyNote) throws URISyntaxException {
        log.debug("REST request to update FamilyNote : {}", familyNote);
        if (familyNote.getId() == null) {
            return createFamilyNote(familyNote);
        }
        FamilyNote result = familyNoteRepository.save(familyNote);
        familyNoteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("familyNote", familyNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /family-notes : get all the familyNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of familyNotes in body
     */
    @RequestMapping(value = "/family-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FamilyNote> getAllFamilyNotes() {
        log.debug("REST request to get all FamilyNotes");
        List<FamilyNote> familyNotes = familyNoteRepository.findAllWithEagerRelationships();
        return familyNotes;
    }

    /**
     * GET  /family-notes/:id : get the "id" familyNote.
     *
     * @param id the id of the familyNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familyNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/family-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> getFamilyNote(@PathVariable Long id) {
        log.debug("REST request to get FamilyNote : {}", id);
        FamilyNote familyNote = familyNoteRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(familyNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /family-notes/:id : delete the "id" familyNote.
     *
     * @param id the id of the familyNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/family-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFamilyNote(@PathVariable Long id) {
        log.debug("REST request to delete FamilyNote : {}", id);
        familyNoteRepository.delete(id);
        familyNoteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("familyNote", id.toString())).build();
    }

    /**
     * SEARCH  /_search/family-notes?query=:query : search for the familyNote corresponding
     * to the query.
     *
     * @param query the query of the familyNote search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/family-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FamilyNote> searchFamilyNotes(@RequestParam String query) {
        log.debug("REST request to search FamilyNotes for query {}", query);
        return StreamSupport
            .stream(familyNoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
