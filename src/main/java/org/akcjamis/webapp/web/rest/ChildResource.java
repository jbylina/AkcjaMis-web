package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Child;
import org.akcjamis.webapp.repository.ChildRepository;
import org.akcjamis.webapp.repository.search.ChildSearchRepository;
import org.akcjamis.webapp.service.FamilyService;
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
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private ChildRepository childRepository;

    @Inject
    private ChildSearchRepository childSearchRepository;

    private FamilyService familyService;

    @Inject
    public ChildResource(ChildRepository childRepository,
                         FamilyService familyService) {
        this.childRepository = childRepository;
        this.familyService = familyService;
    }

    /**
     * POST  /families/:id/children : Create a new child.
     *
     * @param id family ID
     * @param child the child to create
     * @return the ResponseEntity with status 201 (Created) and with body the new child, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/children",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> createChild(@PathVariable Long id, @Valid @RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        if (child.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("child", "idexists", "A new child cannot already have an ID")).body(null);
        }
        Child result = familyService.saveChild(id, child);
        return ResponseEntity.created(new URI("/api/families/" + id + "/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("child", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /families/:id/children : Updates an existing child.
     *
     * @param id family ID
     * @param child the child to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated child,
     * or with status 400 (Bad Request) if the child is not valid,
     * or with status 500 (Internal Server Error) if the child couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/children",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @Valid @RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to update Child : {}", child);
        if (child.getId() == null) {
            return createChild(id, child);
        }
        Child result = familyService.saveChild(id, child);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("child", child.getId().toString()))
            .body(result);
    }

    /**
     * GET  /families/:id/children : get all the children for given family.
     *
     * @param id family ID
     * @return the ResponseEntity with status 200 (OK) and the list of children in body
     */
    @RequestMapping(value = "/families/{id}/children",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Child> getAllChildren(@PathVariable Long id) {
        log.debug("REST request to get all Children");
        return familyService.getAllChildren(id);
    }

    /**
     * GET  /families/:familyId/children/:id : get the "id" child.
     *
     * @param familyId the id of the family
     * @param id the id of the child to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the child, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/families/{familyId}/children/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> getChild(@PathVariable Long familyId, @PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Child child = childRepository.findByIdAndFamily_id(id, familyId);
        return Optional.ofNullable(child)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /families/:familyId/children/:id : delete the "id" child.
     *
     * @param familyId the id of the family
     * @param id the id of the child to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/families/{familyId}/children/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChild(@PathVariable Long familyId, @PathVariable Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.delete(id);
        childSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("child", id.toString())).build();
    }

    /**
     * SEARCH  /_search/children?query=:query : search for the child corresponding
     * to the query.
     *
     * @param query the query of the child search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/children",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Child> searchChildren(@RequestParam String query) {
        log.debug("REST request to search Children for query {}", query);
        return StreamSupport
            .stream(childSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
