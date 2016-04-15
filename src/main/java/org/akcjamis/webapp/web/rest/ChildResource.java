package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Child;
import org.akcjamis.webapp.repository.ChildRepository;
import org.akcjamis.webapp.repository.search.ChildSearchRepository;
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
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);
        
    @Inject
    private ChildRepository childRepository;
    
    @Inject
    private ChildSearchRepository childSearchRepository;
    
    /**
     * POST  /children : Create a new child.
     *
     * @param child the child to create
     * @return the ResponseEntity with status 201 (Created) and with body the new child, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/children",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> createChild(@RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        if (child.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("child", "idexists", "A new child cannot already have an ID")).body(null);
        }
        Child result = childRepository.save(child);
        childSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("child", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /children : Updates an existing child.
     *
     * @param child the child to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated child,
     * or with status 400 (Bad Request) if the child is not valid,
     * or with status 500 (Internal Server Error) if the child couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/children",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> updateChild(@RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to update Child : {}", child);
        if (child.getId() == null) {
            return createChild(child);
        }
        Child result = childRepository.save(child);
        childSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("child", child.getId().toString()))
            .body(result);
    }

    /**
     * GET  /children : get all the children.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of children in body
     */
    @RequestMapping(value = "/children",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Child> getAllChildren() {
        log.debug("REST request to get all Children");
        List<Child> children = childRepository.findAll();
        return children;
    }

    /**
     * GET  /children/:id : get the "id" child.
     *
     * @param id the id of the child to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the child, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/children/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Child child = childRepository.findOne(id);
        return Optional.ofNullable(child)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /children/:id : delete the "id" child.
     *
     * @param id the id of the child to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/children/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
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
