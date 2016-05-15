package org.akcjamis.webapp.service;

import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Events.
 */
@Service
@Transactional
public class EventService {

    private EventRepository eventRepository;

    @Inject
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event){
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Event getEvent(Short year){
        return eventRepository.findOne(year);
    }

    public void deleteEvent(Short year){
        eventRepository.delete(year);
    }
}
