package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.service.ChristmasPackageService;
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
 * REST controller for managing ChristmasPackageNote.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageNoteResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageNoteResource.class);

    private ChristmasPackageNoteRepository christmasPackageNoteRepository;

    private ChristmasPackageService christmasPackageService;

    @Inject
    public ChristmasPackageNoteResource(ChristmasPackageNoteRepository christmasPackageNoteRepository,
                                        ChristmasPackageService christmasPackageService) {
        this.christmasPackageNoteRepository = christmasPackageNoteRepository;
        this.christmasPackageService = christmasPackageService;
    }

    /**
     * POST  /christmas-package/:id/notes : Create a new christmasPackageNote for given package.
     *
     * @param id christmasPackage ID
     * @param christmasPackageNote the christmasPackageNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackageNote, or with status 400 (Bad Request) if the christmasPackageNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-packages/{id}/notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> createChristmasPackageNote(@PathVariable Integer id, @Valid @RequestBody ChristmasPackageNote christmasPackageNote) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackageNote : {}", christmasPackageNote);
        if (christmasPackageNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackageNote", "idexists", "A new christmasPackageNote cannot already have an ID")).body(null);
        }
        ChristmasPackageNote result = christmasPackageService.saveNote(id, christmasPackageNote);
        return ResponseEntity.created(new URI("/api/christmas-packages/" + id + "notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackageNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /christmas-package/:id/notes : Updates an existing christmasPackageNote.
     *
     * @param id christmasPackage ID
     * @param christmasPackageNote the christmasPackageNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackageNote,
     * or with status 400 (Bad Request) if the christmasPackageNote is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackageNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-packages/{id}/notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> updateChristmasPackageNote(@PathVariable Integer id, @Valid @RequestBody ChristmasPackageNote christmasPackageNote) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackageNote : {}", christmasPackageNote);
        if (christmasPackageNote.getId() == null) {
           return createChristmasPackageNote(id, christmasPackageNote);
        }
        ChristmasPackageNote result = christmasPackageService.saveNote(id, christmasPackageNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackageNote", christmasPackageNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /christmas-packages/:id/notes : get all the christmasPackageNotes for given package.
     *
     * @param id christmasPackage ID
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackageNotes in body
     */
    @RequestMapping(value = "/christmas-packages/{id}/notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChristmasPackageNote> getAllChristmasPackageNotes(@PathVariable Integer id) {
        log.debug("REST request to get all ChristmasPackageNotes");
        return christmasPackageService.getAllPackageNotes(id);
    }

    /**
     * GET  /christmas-packages/:packageId/notes/:id : get the "id" christmasPackageNote.
     *
     * @param packageId the id of the christmas package
     * @param id the id of the christmasPackageNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackageNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/christmas-packages/{packageId}/notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> getChristmasPackageNote(@PathVariable Integer packageId, @PathVariable Integer id) {
        log.debug("REST request to get ChristmasPackageNote : {}", id);
        ChristmasPackageNote christmasPackageNote = christmasPackageNoteRepository.findByIdAndChristmasPackage_id(id, packageId);
        return Optional.ofNullable(christmasPackageNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /christmas-packages/:packageId/notes/:id : delete the "id" christmasPackageNote.
     *
     * @param packageId the id of the christmas package
     * @param id the id of the christmasPackageNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/christmas-packages/{packageId}/notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackageNote(@PathVariable Integer packageId, @PathVariable Integer id) {
        log.debug("REST request to delete ChristmasPackageNote : {}", id);
        ChristmasPackage christmasPackage = christmasPackageService.findOneById(packageId);

        if (christmasPackage == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        christmasPackageService.deletePackageNote(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackageNote", id.toString())).build();
    }
}
