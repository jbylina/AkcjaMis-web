package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.*;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.EventRepository;
import org.akcjamis.webapp.repository.FamilyRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageNoteSearchRepository;

import org.akcjamis.webapp.service.ChristmasPackageService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ChristmasPackageNoteResource REST controller.
 *
 * @see ChristmasPackageNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChristmasPackageNoteResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final String DEFAULT_CONTENT = "AAAAAAAA";
    private static final String DEFAULT_CONTENT_SECOND = "AAAAANNMM";
    private static final String UPDATED_CONTENT = "BBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String DEFAULT_POSTALCODE = "AAAAAA";
    private static final String DEFAULT_DISTRICT = "AAAAA";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String DEFAULT_SOURCE = "AAAAA";

    private static final Short DEFAULT_YEAR = 2016;

    @Inject
    private ChristmasPackageNoteRepository christmasPackageNoteRepository;

    @Inject
    private ChristmasPackageRepository christmasPackageRepository;

    @Inject
    private ChristmasPackageNoteSearchRepository christmasPackageNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private FamilyRepository familyRepository;


    private MockMvc restChristmasPackageNoteMockMvc;

    private ChristmasPackageNote christmasPackageNote;
    private ChristmasPackageNote christmasPackageNoteSecond;

    @Inject
    private ChristmasPackageService christmasPackageService;

    private ChristmasPackage christmasPackage;
    private ChristmasPackage christmasPackageSecond;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageNoteResource christmasPackageNoteResource = new ChristmasPackageNoteResource(christmasPackageNoteRepository,christmasPackageService);
        ReflectionTestUtils.setField(christmasPackageNoteResource, "christmasPackageNoteSearchRepository", christmasPackageNoteSearchRepository);
        this.restChristmasPackageNoteMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        christmasPackageNoteSearchRepository.deleteAll();
        christmasPackageNote = new ChristmasPackageNote();
        christmasPackageNote.setContent(DEFAULT_CONTENT);
        christmasPackageNoteSecond = new ChristmasPackageNote();
        christmasPackageNoteSecond.setContent(DEFAULT_CONTENT_SECOND);

        eventRepository.deleteAll();
        Event event = new Event();
        event.setYear(DEFAULT_YEAR);
        event = eventRepository.save(event);

        familyRepository.deleteAll();
        Family family = new Family();
        family.setStreet(DEFAULT_STREET);
        family.setHouseNo(DEFAULT_HOUSE_NO);
        family.setFlatNo(DEFAULT_FLAT_NO);
        family.setPostalcode(DEFAULT_POSTALCODE);
        family.setDistrict(DEFAULT_DISTRICT);
        family.setCity(DEFAULT_CITY);
        family.setRegion(DEFAULT_REGION);
        family.setSource(DEFAULT_SOURCE);
        familyRepository.save(family);

        christmasPackageRepository.deleteAll();
        christmasPackage = new ChristmasPackage();
        christmasPackage.setPackageNumber(DEFAULT_NUMBER);
        christmasPackage.setDelivered(false);
        christmasPackage.setMark(DEFAULT_NUMBER);
        christmasPackage.setCreatedDate(ZonedDateTime.now());
        christmasPackage.setEvent(event);
        christmasPackage.setFamily(family);

        christmasPackage = christmasPackageRepository.save(christmasPackage);

        christmasPackageSecond = new ChristmasPackage();
        christmasPackageSecond.setPackageNumber(DEFAULT_NUMBER);
        christmasPackageSecond.setDelivered(false);
        christmasPackageSecond.setMark(DEFAULT_NUMBER);
        christmasPackageSecond.setCreatedDate(ZonedDateTime.now());
        christmasPackageSecond.setEvent(event);
        christmasPackageSecond.setFamily(family);

        christmasPackage = christmasPackageRepository.save(christmasPackageSecond);
    }

    @Test
    @Transactional
    public void createChristmasPackageNote() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageNoteRepository.findAll().size();

        // Create the ChristmasPackageNote

//
        restChristmasPackageNoteMockMvc.perform(post("/api/christmas-packages/{id}/notes", christmasPackage.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageNote)))
                .andExpect(status().isCreated());

        // Validate the ChristmasPackageNote in the database
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeCreate + 1);
        ChristmasPackageNote testChristmasPackageNote = christmasPackageNotes.get(christmasPackageNotes.size() - 1);
        assertThat(testChristmasPackageNote.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageNoteRepository.findAll().size();
        // set the field null
        christmasPackageNote.setContent(null);

        // Create the ChristmasPackageNote, which fails.

        restChristmasPackageNoteMockMvc.perform(post("/api/christmas-packages/{id}/notes", christmasPackage.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageNote)))
                .andExpect(status().isBadRequest());

        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChristmasPackageNotes() throws Exception {
        // Initialize the database
        christmasPackageNote.setChristmasPackage(christmasPackage);
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);

        // Get all the christmasPackageNotes
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-packages/{id}/notes?sort=id,desc", christmasPackage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNote.setChristmasPackage(christmasPackage);
        christmasPackageNoteSecond.setChristmasPackage(christmasPackageSecond);
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNoteSecond);

        // Get the first christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-packages/{packageId}/notes/{id}", christmasPackage.getId(), christmasPackageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackageNote.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));

        // Get the second christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-packages/{packageId}/notes/{id}", christmasPackageSecond.getId(), christmasPackageNoteSecond.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackageNoteSecond.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT_SECOND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackageNote() throws Exception {
        // Get the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-packages/{packageId}/notes/{id}", Long.MAX_VALUE, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNote.setChristmasPackage(christmasPackage);

        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);

        int databaseSizeBeforeUpdate = christmasPackageNoteRepository.findAll().size();

        // Update the christmasPackageNote
        ChristmasPackageNote updatedChristmasPackageNote = new ChristmasPackageNote();
        updatedChristmasPackageNote.setId(christmasPackageNote.getId());
        updatedChristmasPackageNote.setContent(UPDATED_CONTENT);

        restChristmasPackageNoteMockMvc.perform(put("/api/christmas-packages/{id}/notes", christmasPackage.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChristmasPackageNote)))
                .andExpect(status().isOk());

        // Validate the ChristmasPackageNote in the database
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeUpdate);
        ChristmasPackageNote testChristmasPackageNote = christmasPackageNotes.get(christmasPackageNotes.size() - 1);
        assertThat(testChristmasPackageNote.getContent()).isEqualTo(UPDATED_CONTENT);

    }

    @Test
    @Transactional
    public void deleteChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNote.setChristmasPackage(christmasPackage);

        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);

        int databaseSizeBeforeDelete = christmasPackageNoteRepository.findAll().size();

        restChristmasPackageNoteMockMvc.perform(delete("/api/christmas-packages/{packageId}/notes/{id}", Long.MAX_VALUE, christmasPackageNote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());

        // Get the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(delete("/api/christmas-packages/{packageId}/notes/{id}", christmasPackage.getId(), christmasPackageNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNote.setChristmasPackage(christmasPackage);

        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);
        christmasPackageNoteSearchRepository.save(christmasPackageNote);

        // Search the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/_search/christmas-package-notes?query=id:" + christmasPackageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
}
