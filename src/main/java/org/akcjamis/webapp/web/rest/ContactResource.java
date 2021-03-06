package org.akcjamis.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.akcjamis.webapp.domain.Contact;
import org.akcjamis.webapp.repository.ContactRepository;
import org.akcjamis.webapp.service.FamilyService;
import org.akcjamis.webapp.web.rest.dto.ContactDTO;
import org.akcjamis.webapp.web.rest.mapper.ContactMapper;
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
 * REST controller for managing Contact.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private ContactRepository contactRepository;

    private FamilyService familyService;

    private ContactMapper mapper;

    @Inject
    public ContactResource(ContactRepository contactRepository,
                           FamilyService familyService,
                           ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.familyService = familyService;
        this.mapper = contactMapper;
    }

    /**
     * POST  /families/:id/contacts : Create a new contact for given family
     *
     * @param id family ID
     * @param contact the contact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contact, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/contacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactDTO> createContact(@PathVariable Integer id, @Valid @RequestBody ContactDTO contact) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contact);
        if (contact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contact", "idexists", "A new contact cannot already have an ID")).body(null);
        }
        Contact result = familyService.saveContact(id, mapper.toContact(contact));
        return ResponseEntity.created(new URI("/api/families/" + id + "/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contact", result.getId().toString()))
            .body(mapper.toContactDTO(result));
    }

    /**
     * PUT  /families/:id/contacts : Updates an existing contact.
     *
     * @param id family ID
     * @param contact the contact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contact,
     * or with status 400 (Bad Request) if the contact is not valid,
     * or with status 500 (Internal Server Error) if the contact couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/families/{id}/contacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Integer id, @Valid @RequestBody ContactDTO contact) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contact);
        if (contact.getId() == null) {
            return createContact(id, contact);
        }
        Contact result = familyService.saveContact(id, mapper.toContact(contact));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contact", contact.getId().toString()))
            .body(mapper.toContactDTO(result));
    }

    /**
     * GET  /families/:id/contacts : get all the contacts for given family.
     *
     * @param id family ID
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @RequestMapping(value = "/families/{id}/contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactDTO>> getAllContacts(@PathVariable Integer id) {
        log.debug("REST request to get all Contacts");

        List<Contact> contacts = familyService.getAllContacts(id);

        return Optional.ofNullable(mapper.toContactDTOs(contacts))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /families/:familyId/contacts/:id : get the "id" contact.
     *
     * @param familyId the id of the family
     * @param id the id of the contact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contact, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/families/{familyId}/contacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactDTO> getContact(@PathVariable Integer familyId, @PathVariable Integer id) {
        log.debug("REST request to get Contact : {}", id);
        Contact contact = contactRepository.findByIdAndFamily_id(id, familyId);
        return Optional.ofNullable(mapper.toContactDTO(contact))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /families/:familyId/contacts/:id : delete the "id" contact.
     *
     * @param familyId the id of the family
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/families/{familyId}/contacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContact(@PathVariable Long familyId, @PathVariable Integer id) {
        log.debug("REST request to delete Contact : {}", id);
        contactRepository.delete(id); // TODO move to FamilyService ?
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contact", id.toString())).build();
    }
}
