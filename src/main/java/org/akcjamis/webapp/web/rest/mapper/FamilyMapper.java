package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.Contact;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.domain.Tag;
import org.akcjamis.webapp.repository.TagRepository;
import org.akcjamis.webapp.web.rest.dto.ContactDTO;
import org.akcjamis.webapp.web.rest.dto.FamilyDTO;
import org.akcjamis.webapp.web.rest.dto.FamilyNoteDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfiguration.class)
public abstract class FamilyMapper {

    // Family
    public abstract FamilyDTO toFamilyDTO(Family family);

    @InheritConfiguration(name = "ignoreAuditing")
    public abstract Family toFamily(FamilyDTO familyDTO);

    public abstract List<FamilyDTO> toFamilyDTOs(List<Family> families);

    // Contact
    public abstract ContactDTO toContactDTO(Contact contact);

    @InheritConfiguration(name = "ignoreAuditing")
    public abstract Contact toContact(ContactDTO contactDTO);

    public abstract List<ContactDTO> toContactDTOs(List<Contact> contacts);

    // FamilyNote
    @Inject
    TagRepository tagRepository;

    @Mapping(target = "family", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @InheritConfiguration(name = "ignoreAuditing")
    public abstract FamilyNote toFamilyNote(FamilyNoteDTO familyNoteDTO);

    public Set<Tag> tagsFromStrings(Set<String> tags) {
        return tagRepository.findByCodeIn(tags);
    }

    public abstract FamilyNoteDTO toFamilyNoteDTO(FamilyNote familyNote);

    public Set<String> stringsFromTags(Set<Tag> tags) {
        return tags.stream().map(Tag::getCode).collect(Collectors.toSet());
    }


}
