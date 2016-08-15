package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.service.ChristmasPackageService;
import org.akcjamis.webapp.web.rest.dto.ChristmasPackageDTO;
import org.akcjamis.webapp.web.rest.dto.ChristmasPackageMarkDTO;
import org.akcjamis.webapp.web.rest.mapper.EventMapper;
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
 * REST controller for managing ChristmasPackage.
 */
@RestController
@RequestMapping("/api")
public class ChristmasPackageResource {

    private final Logger log = LoggerFactory.getLogger(ChristmasPackageResource.class);

    private ChristmasPackageService christmasPackageService;

    private EventMapper mapper;

    @Inject
    public ChristmasPackageResource(ChristmasPackageService christmasPackageService,
                                    EventMapper eventMapper) {
        this.christmasPackageService = christmasPackageService;
        this.mapper = eventMapper;
    }

    /**
     * POST  /events/:year/christmas-packages : Create a new christmasPackage.
     *
     * @param year the event Id
     * @param christmasPackageDTO the christmasPackage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new christmasPackage, or with status 400 (Bad Request) if the christmasPackage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/events/{year}/christmas-packages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageDTO> createChristmasPackage(@PathVariable Short year, @Valid @RequestBody ChristmasPackageDTO christmasPackageDTO) throws URISyntaxException {
        log.debug("REST request to save ChristmasPackage : {}", christmasPackageDTO);
        if (christmasPackageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("christmasPackage", "idexists", "A new christmasPackage cannot already have an ID")).body(null);
        }
        ChristmasPackage result = christmasPackageService.savePackage(year, mapper.toChristmasPackage(christmasPackageDTO));
        return ResponseEntity.created(new URI("/api/events/" + year + "/christmas-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("christmasPackage", result.getId().toString()))
            .body(mapper.toChristmasPackageDTO(result));
    }

    /**
     * PUT  /events/:year/christmas-packages : Updates an existing christmasPackage.
     *
     * @param year year of the event
     * @param christmasPackageDTO the christmasPackage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated christmasPackage,
     * or with status 400 (Bad Request) if the christmasPackage is not valid,
     * or with status 500 (Internal Server Error) if the christmasPackage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/events/{year}/christmas-packages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageDTO> updateChristmasPackage(@PathVariable Short year, @Valid @RequestBody ChristmasPackageDTO christmasPackageDTO) throws URISyntaxException {
        log.debug("REST request to update ChristmasPackage : {}", christmasPackageDTO);
        if (christmasPackageDTO.getId() == null) {
            return createChristmasPackage(year, christmasPackageDTO);
        }
        ChristmasPackage result = christmasPackageService.savePackage(year, mapper.toChristmasPackage(christmasPackageDTO));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackage", christmasPackageDTO.getId().toString()))
            .body(mapper.toChristmasPackageDTO(result));
    }

    /**
     * GET  /events/:year/christmas-packages : get all the christmasPackages for given event.
     *
     * @param year year of the event
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of christmasPackages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/events/{year}/christmas-packages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackageDTO>> getAllChristmasPackages(@PathVariable Short year, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChristmasPackages");
        Page<ChristmasPackage> page = christmasPackageService.findAll(year, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events/" + year + "/christmas-packages");
        return new ResponseEntity<>(mapper.toChristmasPackageDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:eventId/christmas-packages-list : get the "id" christmasPackageList.
     *
     * @param year year of the event
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackage, or with status 404 (Not Found)
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/events/{year}/christmas-packages-list",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChristmasPackageDTO>> getChristmasPackageList(@PathVariable Short year, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get ChristmasPackageList : {}");

        Page<ChristmasPackage> page = christmasPackageService.getList(year, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events/" + year + "/christmas-packages-list");
        return new ResponseEntity<>(mapper.toChristmasPackageDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:year/christmas-packages/:id : get the "id" christmasPackage.
     *
     * @param year year of the event
     * @param id the id of the christmasPackage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the christmasPackage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/events/{year}/christmas-packages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackageDTO> getChristmasPackage(@PathVariable Short year, @PathVariable Integer id) {
        log.debug("REST request to get ChristmasPackage : {}", id);
        ChristmasPackage christmasPackage = christmasPackageService.findOne(year, id);
        return Optional.ofNullable(mapper.toChristmasPackageDTO(christmasPackage))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /events/:year/christmas-packages/:id : delete the "id" christmasPackage.
     *
     * @param year year of the event
     * @param id the id of the christmasPackage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/events/{year}/christmas-packages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChristmasPackage(@PathVariable Long year, @PathVariable Integer id) {
        log.debug("REST request to delete ChristmasPackage : {}", id);
        christmasPackageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("christmasPackage", id.toString())).build();
    }


    /**
     * POST  /events/:year/christmas-packages/:id :
     */
    @RequestMapping(value = "/events/christmas-packages/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChristmasPackage> updateChristmasPackage(@PathVariable Integer id, @RequestBody ChristmasPackageMarkDTO packageMark) throws URISyntaxException {
        log.debug("REST request to update mark of ChristmasPackage  : {}", id);

        ChristmasPackage christmasPackage = christmasPackageService.findOne(id);
        christmasPackage.setMark(packageMark.getMark());
        christmasPackage = christmasPackageService.savePackage(christmasPackage.getEvent().getYear(), christmasPackage);
        ChristmasPackageNote note = new ChristmasPackageNote();
        note.setContent(packageMark.getNoteText());
        christmasPackageService.saveNote(id, note);


        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("christmasPackage", christmasPackage.getId().toString()))
            .body(christmasPackage);
    }

}
