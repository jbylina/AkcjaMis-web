package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.service.EventService;
import org.akcjamis.webapp.web.rest.dto.EventDTO;
import org.akcjamis.webapp.web.rest.mapper.EventMapper;
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
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private EventService eventService;

    private EventMapper mapper;

    @Inject
    public EventResource(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.mapper = eventMapper;
    }

    /**
     * POST  /events : Create a new event.
     *
     * @param event the event to create
     * @return the ResponseEntity with status 201 (Created) and with body the new event, or with status 400 (Bad Request) if the event has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO event) throws URISyntaxException {
        log.debug("REST request to save Event : {}", event);

        Event result = eventService.createEvent(mapper.toEvent(event));

        return ResponseEntity.created(new URI("/api/events/" + result.getYear()))
            .headers(HeaderUtil.createEntityCreationAlert("event", result.getYear().toString()))
            .body(mapper.eventToEventDTO(result));
    }

    /**
     * GET  /events : get all the events.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EventDTO> getAllEvents() {
        log.debug("REST request to get all Events");
        return mapper.toEventDTOs(eventService.getAllEvents());
    }

    /**
     * GET  /events/:year : get the "year" event.
     *
     * @param year the year of the event to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the event, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/events/{year}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> getEvent(@PathVariable Short year) {
        log.debug("REST request to get Event : {}", year);
        Event event = eventService.getEvent(year);
        return Optional.ofNullable(mapper.eventToEventDTO(event))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /events/:year : delete the "year" event.
     *
     * @param year the id of the event to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/events/{year}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEvent(@PathVariable Short year) {
        log.debug("REST request to delete Event : {}", year);
        eventService.deleteEvent(year);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event", year.toString())).build();
    }
}
