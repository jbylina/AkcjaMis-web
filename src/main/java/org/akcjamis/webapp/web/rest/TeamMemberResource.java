package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.TeamMember;
import org.akcjamis.webapp.repository.TeamMemberRepository;
import org.akcjamis.webapp.repository.search.TeamMemberSearchRepository;
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
 * REST controller for managing TeamMember.
 */
@RestController
@RequestMapping("/api")
public class TeamMemberResource {

    private final Logger log = LoggerFactory.getLogger(TeamMemberResource.class);
        
    @Inject
    private TeamMemberRepository teamMemberRepository;
    
    @Inject
    private TeamMemberSearchRepository teamMemberSearchRepository;
    
    /**
     * POST  /team-members : Create a new teamMember.
     *
     * @param teamMember the teamMember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamMember, or with status 400 (Bad Request) if the teamMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/team-members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TeamMember> createTeamMember(@RequestBody TeamMember teamMember) throws URISyntaxException {
        log.debug("REST request to save TeamMember : {}", teamMember);
        if (teamMember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("teamMember", "idexists", "A new teamMember cannot already have an ID")).body(null);
        }
        TeamMember result = teamMemberRepository.save(teamMember);
        teamMemberSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/team-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("teamMember", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-members : Updates an existing teamMember.
     *
     * @param teamMember the teamMember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamMember,
     * or with status 400 (Bad Request) if the teamMember is not valid,
     * or with status 500 (Internal Server Error) if the teamMember couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/team-members",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TeamMember> updateTeamMember(@RequestBody TeamMember teamMember) throws URISyntaxException {
        log.debug("REST request to update TeamMember : {}", teamMember);
        if (teamMember.getId() == null) {
            return createTeamMember(teamMember);
        }
        TeamMember result = teamMemberRepository.save(teamMember);
        teamMemberSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("teamMember", teamMember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-members : get all the teamMembers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teamMembers in body
     */
    @RequestMapping(value = "/team-members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TeamMember> getAllTeamMembers() {
        log.debug("REST request to get all TeamMembers");
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        return teamMembers;
    }

    /**
     * GET  /team-members/:id : get the "id" teamMember.
     *
     * @param id the id of the teamMember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamMember, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/team-members/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TeamMember> getTeamMember(@PathVariable Long id) {
        log.debug("REST request to get TeamMember : {}", id);
        TeamMember teamMember = teamMemberRepository.findOne(id);
        return Optional.ofNullable(teamMember)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /team-members/:id : delete the "id" teamMember.
     *
     * @param id the id of the teamMember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/team-members/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        log.debug("REST request to delete TeamMember : {}", id);
        teamMemberRepository.delete(id);
        teamMemberSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("teamMember", id.toString())).build();
    }

    /**
     * SEARCH  /_search/team-members?query=:query : search for the teamMember corresponding
     * to the query.
     *
     * @param query the query of the teamMember search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/team-members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TeamMember> searchTeamMembers(@RequestParam String query) {
        log.debug("REST request to search TeamMembers for query {}", query);
        return StreamSupport
            .stream(teamMemberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
