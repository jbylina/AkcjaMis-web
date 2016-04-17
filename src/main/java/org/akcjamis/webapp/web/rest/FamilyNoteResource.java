package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.service.FamilyNoteService;
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
import java.util.Objects;
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

    private FamilyNoteService familyNoteService;

    @Inject
    public FamilyNoteResource(FamilyNoteService familyNoteService) {
        this.familyNoteService = familyNoteService;
    }

    /**
     * POST  /families/:id/family-notes : Create a new familyNote.
     *
     * @param familyNote the familyNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familyNote, or with status 400 (Bad Request) if the familyNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/family-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> createFamilyNote(@PathVariable Long id, @Valid @RequestBody FamilyNote familyNote) throws URISyntaxException {
        log.debug("REST request to save FamilyNote : {}", familyNote);
        if (familyNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("familyNote", "idexists", "A new familyNote cannot already have an ID")).body(null);
        }
        FamilyNote result = familyNoteService.save(id, familyNote);
        return ResponseEntity.created(new URI("/api/families/" + id + "/family-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("familyNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /families/:id/family-notes : Updates an existing familyNote.
     *
     * @param id family ID
     * @param familyNote the familyNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familyNote,
     * or with status 400 (Bad Request) if the familyNote is not valid,
     * or with status 500 (Internal Server Error) if the familyNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/family-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> updateFamilyNote(@PathVariable Long id, @Valid @RequestBody FamilyNote familyNote) throws URISyntaxException {
        log.debug("REST request to update FamilyNote : {}", familyNote);
        if (familyNote.getId() == null) {
            return createFamilyNote(id, familyNote);
        }
        FamilyNote result = familyNoteService.save(id, familyNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("familyNote", familyNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /families/:id/family-notes : get all the familyNotes for given family.
     *
     * @param id family ID
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of familyNotes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/families/{id}/family-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FamilyNote>> getAllFamilyNotes(@PathVariable Long id, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FamilyNotes");
        Page<FamilyNote> page = familyNoteService.findAll(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/families/" + id + "/family-notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /families/:familyId/family-notes/:id : get the "id" familyNote.
     *
     * @param familyId the id of the family
     * @param id the id of the familyNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familyNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/families/{familyId}/family-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FamilyNote> getFamilyNote(@PathVariable Long familyId, @PathVariable Long id) {
        log.debug("REST request to get FamilyNote : {}", id);
        FamilyNote familyNote = familyNoteService.findOne(id);
        return Optional.ofNullable(familyNote)
            .filter(note -> Objects.equals(note.getFamily().getId(), familyId))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /families/:familyId/family-notes/:id : delete the "id" familyNote.
     *
     * @param id the id of the familyNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/families/{familyId}/family-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFamilyNote(@PathVariable Long familyId, @PathVariable Long id) {
        log.debug("REST request to delete FamilyNote : {}", id);
        familyNoteService.delete(id);
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
    public ResponseEntity<List<FamilyNote>> searchFamilyNotes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of FamilyNotes for query {}", query);
        Page<FamilyNote> page = familyNoteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/family-notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}