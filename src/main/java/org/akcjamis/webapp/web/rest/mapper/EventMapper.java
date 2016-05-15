package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.web.rest.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDTO eventToEventDTO(Event event);

    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "christmasPackages", ignore = true)
    Event eventDTOToEvent(EventDTO eventDTO);

    List<EventDTO> eventsToEventDTOs(List<Event> events);
}
