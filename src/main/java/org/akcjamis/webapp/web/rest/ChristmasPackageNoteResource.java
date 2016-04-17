package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageNoteSearchRepository;
import org.akcjamis.webapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing ChristmasPackageNote.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageNoteResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageNoteResource.class);
        
    @Inject
    private ChristmasPackageNoteRepository christmasPackageNoteRepository;
    
    @Inject
    private ChristmasPackageNoteSearchRepository christmasPackageNoteSearchRepository;
    
    /**
     * POST  /christmas-package-notes : Create a new christmasPackageNote.
     *
     * @param christmasPackageNote the christmasPackageNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackageNote, or with status 400 (Bad Request) if the christmasPackageNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> createChristmasPackageNote(@Valid @RequestBody ChristmasPackageNote christmasPackageNote) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackageNote : {}", christmasPackageNote);
        if (christmasPackageNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackageNote", "idexists", "A new christmasPackageNote cannot already have an ID")).body(null);
        }
        ChristmasPackageNote result = christmasPackageNoteRepository.save(christmasPackageNote);
        christmasPackageNoteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/christmas-package-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackageNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /christmas-package-notes : Updates an existing christmasPackageNote.
     *
     * @param christmasPackageNote the christmasPackageNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackageNote,
     * or with status 400 (Bad Request) if the christmasPackageNote is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackageNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/christmas-package-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> updateChristmasPackageNote(@Valid @RequestBody ChristmasPackageNote christmasPackageNote) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackageNote : {}", christmasPackageNote);
        if (christmasPackageNote.getId() == null) {
            return createChristmasPackageNote(christmasPackageNote);
        }
        ChristmasPackageNote result = christmasPackageNoteRepository.save(christmasPackageNote);
        christmasPackageNoteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackageNote", christmasPackageNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /christmas-package-notes : get all the christmasPackageNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackageNotes in body
     */
    @RequestMapping(value = "/christmas-package-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChristmasPackageNote> getAllChristmasPackageNotes() {
        log.debug("REST request to get all ChristmasPackageNotes");
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        return christmasPackageNotes;
    }

    /**
     * GET  /christmas-package-notes/:id : get the "id" christmasPackageNote.
     *
     * @param id the id of the christmasPackageNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackageNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/christmas-package-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageNote> getChristmasPackageNote(@PathVariable Long id) {
        log.debug("REST request to get ChristmasPackageNote : {}", id);
        ChristmasPackageNote christmasPackageNote = christmasPackageNoteRepository.findOne(id);
        return Optional.ofNullable(christmasPackageNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /christmas-package-notes/:id : delete the "id" christmasPackageNote.
     *
     * @param id the id of the christmasPackageNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/christmas-package-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackageNote(@PathVariable Long id) {
        log.debug("REST request to delete ChristmasPackageNote : {}", id);
        christmasPackageNoteRepository.delete(id);
        christmasPackageNoteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackageNote", id.toString())).build();
    }

    /**
     * SEARCH  /_search/christmas-package-notes?query=:query : search for the christmasPackageNote corresponding
     * to the query.
     *
     * @param query the query of the christmasPackageNote search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/christmas-package-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChristmasPackageNote> searchChristmasPackageNotes(@RequestParam String query) {
        log.debug("REST request to search ChristmasPackageNotes for query {}", query);
        return StreamSupport
            .stream(christmasPackageNoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
