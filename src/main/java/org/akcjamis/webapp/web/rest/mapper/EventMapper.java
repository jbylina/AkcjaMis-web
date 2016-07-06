package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.web.rest.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventMapper {

    EventDTO eventToEventDTO(Event event);

    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "christmasPackages", ignore = true)
    Event toEvent(EventDTO eventDTO);

    List<EventDTO> toEventDTOs(List<Event> events);
}
