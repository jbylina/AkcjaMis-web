package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.web.rest.dto.ChristmasPackageDTO;
import org.akcjamis.webapp.web.rest.dto.EventDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface EventMapper {

    // Event
    EventDTO toEventDTO(Event event);

    @InheritConfiguration(name = "ignoreAuditing")
    Event toEvent(EventDTO eventDTO);

    List<EventDTO> toEventDTOs(List<Event> events);

    // ChristmasPackage
    @Mapping(source = "family", target = "familyId")
    @Mapping(source = "event", target = "eventYear")
    ChristmasPackageDTO toChristmasPackageDTO(ChristmasPackage event);


    @Mapping(source = "familyId", target = "family")
    @Mapping(source = "eventYear", target = "event")
    ChristmasPackage toChristmasPackage(ChristmasPackageDTO eventDTO);

    List<ChristmasPackageDTO> toChristmasPackageDTOs(List<ChristmasPackage> events);

    default Family familyFromfamilyId(Integer family){
        Family f = new Family();
        f.setId(family);
        return f;
    }

    default Event eventFromEventYear(Short eventYear){
        Event e = new Event();
        e.setYear(eventYear);
        return e;
    }

    default Integer familyIdFromFamily(Family family){
        return family.getId();
    }

    default Short eventYearFromEvent(Event event){
        return event.getYear();
    }
}
