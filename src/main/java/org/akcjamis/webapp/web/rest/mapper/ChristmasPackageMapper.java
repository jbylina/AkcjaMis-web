package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.web.rest.dto.ChristmasPackageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChristmasPackageMapper {

    ChristmasPackageMapper INSTANCE = Mappers.getMapper(ChristmasPackageMapper.class);

    @Mappings({
        @Mapping(source = "family", target = "familyId"),
        @Mapping(source = "event", target = "eventYear")
    })
    ChristmasPackageDTO toChristmasPackageDTO(ChristmasPackage event);

    @Mappings({
        @Mapping(target = "christmasPackageChanges", ignore = true),
        @Mapping(target = "team", ignore = true),
        @Mapping(source = "familyId", target = "family"),
        @Mapping(source = "eventYear", target = "event"),
        @Mapping(target = "delivered", ignore = true)
    })
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
