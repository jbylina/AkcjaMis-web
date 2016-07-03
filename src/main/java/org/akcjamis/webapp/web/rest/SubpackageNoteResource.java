package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.SubpackageNote;
import org.akcjamis.webapp.repository.SubpackageNoteRepository;
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
 * REST controller for managing SubpackageNote.
 */
@RestController
@RequestMapping("/api")
public class SubpackageNoteResource {

    private final Logger log = LoggerFactory.getLogger(SubpackageNoteResource.class);

    @Inject
    private SubpackageNoteRepository subpackageNoteRepository;

    /**
     * POST  /subpackage-notes : Create a new subpackageNote.
     *
     * @param subpackageNote the subpackageNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subpackageNote, or with status 400 (Bad Request) if the subpackageNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subpackage-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubpackageNote> createSubpackageNote(@Valid @RequestBody SubpackageNote subpackageNote) throws URISyntaxException {
        log.debug("REST request to save SubpackageNote : {}", subpackageNote);
        if (subpackageNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subpackageNote", "idexists", "A new subpackageNote cannot already have an ID")).body(null);
        }
        SubpackageNote result = subpackageNoteRepository.save(subpackageNote);
        return ResponseEntity.created(new URI("/api/subpackage-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subpackageNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subpackage-notes : Updates an existing subpackageNote.
     *
     * @param subpackageNote the subpackageNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subpackageNote,
     * or with status 400 (Bad Request) if the subpackageNote is not valid,
     * or with status 500 (Internal Server Error) if the subpackageNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subpackage-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubpackageNote> updateSubpackageNote(@Valid @RequestBody SubpackageNote subpackageNote) throws URISyntaxException {
        log.debug("REST request to update SubpackageNote : {}", subpackageNote);
        if (subpackageNote.getId() == null) {
            return createSubpackageNote(subpackageNote);
        }
        SubpackageNote result = subpackageNoteRepository.save(subpackageNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subpackageNote", subpackageNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subpackage-notes : get all the subpackageNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subpackageNotes in body
     */
    @RequestMapping(value = "/subpackage-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SubpackageNote> getAllSubpackageNotes() {
        log.debug("REST request to get all SubpackageNotes");
        List<SubpackageNote> subpackageNotes = subpackageNoteRepository.findAll();
        return subpackageNotes;
    }

    /**
     * GET  /subpackage-notes/:id : get the "id" subpackageNote.
     *
     * @param id the id of the subpackageNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subpackageNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subpackage-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubpackageNote> getSubpackageNote(@PathVariable Integer id) {
        log.debug("REST request to get SubpackageNote : {}", id);
        SubpackageNote subpackageNote = subpackageNoteRepository.findOne(id);
        return Optional.ofNullable(subpackageNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subpackage-notes/:id : delete the "id" subpackageNote.
     *
     * @param id the id of the subpackageNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subpackage-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubpackageNote(@PathVariable Integer id) {
        log.debug("REST request to delete SubpackageNote : {}", id);
        subpackageNoteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subpackageNote", id.toString())).build();
    }

}
