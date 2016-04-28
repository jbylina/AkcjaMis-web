package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.domain.Tag;
import org.akcjamis.webapp.repository.TagRepository;
import org.akcjamis.webapp.web.rest.dto.FamilyNoteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity FamilyNote and its DTO FamilyNoteDTO.
 */

@Mapper(componentModel = "spring", uses = {})
public abstract class FamilyNoteMapper {

    @Inject
    TagRepository tagRepository;

    @Mapping(target = "family", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public abstract FamilyNote toFamilyNote(FamilyNoteDTO familyNoteDTO);

    public Set<Tag> tagsFromStrings(Set<String> tags) {
        return tagRepository.findByCodeIn(tags);
    }

    public abstract FamilyNoteDTO toFamilyNoteDTO(FamilyNote familyNote);

    public Set<String> stringsFromTags(Set<Tag> tags) {
        return tags.stream().map(Tag::getCode).collect(Collectors.toSet());
    }
}
