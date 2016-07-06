package org.akcjamis.webapp.web.rest.family;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Contact;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.repository.ContactRepository;
import org.akcjamis.webapp.repository.FamilyRepository;

import org.akcjamis.webapp.service.FamilyService;
import org.akcjamis.webapp.web.rest.ContactResource;
import org.akcjamis.webapp.web.rest.TestUtil;
import org.akcjamis.webapp.web.rest.mapper.ContactMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ContactResource REST controller.
 *
 * @see ContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContactResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String DEFAULT_POSTALCODE = "AAAAAA";
    private static final String DEFAULT_DISTRICT = "AAAAA";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String DEFAULT_SOURCE = "AAAAA";

    @Inject
    private ContactRepository contactRepository;

    @Inject
    private FamilyService familyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private FamilyRepository familyRepository;

    @Inject
    private ContactMapper mapper;

    private MockMvc restContactMockMvc;

    private Contact contact;

    private Family family;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactResource contactResource = new ContactResource(contactRepository, familyService, mapper);
        this.restContactMockMvc = MockMvcBuilders.standaloneSetup(contactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {

        familyRepository.deleteAll();
        family = new Family();
        family.setStreet(DEFAULT_STREET);
        family.setHouseNo(DEFAULT_HOUSE_NO);
        family.setFlatNo(DEFAULT_FLAT_NO);
        family.setPostalcode(DEFAULT_POSTALCODE);
        family.setDistrict(DEFAULT_DISTRICT);
        family.setCity(DEFAULT_CITY);
        family.setRegion(DEFAULT_REGION);
        family.setSource(DEFAULT_SOURCE);
        family = familyRepository.save(family);

        contact = new Contact();
        contact.setType(DEFAULT_TYPE);
        contact.setValue(DEFAULT_VALUE);
        contact.setComment(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact
        restContactMockMvc.perform(post("/api/families/{id}/contacts", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contact)))
                .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contacts.get(contacts.size() - 1);
        assertThat(testContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContact.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testContact.getComment()).isEqualTo(DEFAULT_COMMENT);

    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setType(null);

        // Create the Contact, which fails.
        restContactMockMvc.perform(post("/api/families/{id}/contacts", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contact)))
                .andExpect(status().isBadRequest());

        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setValue(null);

        // Create the Contact, which fails.

        restContactMockMvc.perform(post("/api/families/{id}/contacts", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contact)))
                .andExpect(status().isBadRequest());

        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        contact.setFamily(family);
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contacts
        restContactMockMvc.perform(get("/api/families/{id}/contacts?sort=id,desc", family.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    public void getContact() throws Exception {
        contact.setFamily(family);
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/families/{id}/contacts/{id}", family.getId(), contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contact.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/families/{id}/contacts/{id}", family.getId(), Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        contact.setFamily(family);
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = new Contact();
        updatedContact.setId(contact.getId());
        updatedContact.setType(UPDATED_TYPE);
        updatedContact.setValue(UPDATED_VALUE);
        updatedContact.setComment(UPDATED_COMMENT);

        restContactMockMvc.perform(put("/api/families/{id}/contacts", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContact)))
                .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contacts.get(contacts.size() - 1);
        assertThat(testContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContact.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testContact.getComment()).isEqualTo(UPDATED_COMMENT);

    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        contact.setFamily(family);
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Get the contact
        restContactMockMvc.perform(delete("/api/families/{id}/contacts/{id}", family.getId(), contact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
