package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.Contact;
import org.akcjamis.webapp.web.rest.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ContactMapper {

    ContactDTO toContactDTO(Contact contact);

    @Mapping(target = "family", ignore = true)
    Contact toContact(ContactDTO contactDTO);

    List<ContactDTO> toContactDTOs(List<Contact> contacts);
}
