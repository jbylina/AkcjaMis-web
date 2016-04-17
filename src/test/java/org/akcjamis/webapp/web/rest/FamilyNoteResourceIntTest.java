package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.repository.FamilyNoteRepository;
import org.akcjamis.webapp.service.FamilyNoteService;
import org.akcjamis.webapp.repository.search.FamilyNoteSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FamilyNoteResource REST controller.
 *
 * @see FamilyNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class FamilyNoteResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    @Inject
    private FamilyNoteRepository familyNoteRepository;

    @Inject
    private FamilyNoteService familyNoteService;

    @Inject
    private FamilyNoteSearchRepository familyNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFamilyNoteMockMvc;

    private FamilyNote familyNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilyNoteResource familyNoteResource = new FamilyNoteResource(familyNoteService);
        this.restFamilyNoteMockMvc = MockMvcBuilders.standaloneSetup(familyNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        familyNoteSearchRepository.deleteAll();
        familyNote = new FamilyNote();
        familyNote.setContent(DEFAULT_CONTENT);
        familyNote.setTime(DEFAULT_TIME);
        familyNote.setArchived(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    public void createFamilyNote() throws Exception {
        int databaseSizeBeforeCreate = familyNoteRepository.findAll().size();

        // Create the FamilyNote

        restFamilyNoteMockMvc.perform(post("/api/family-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNote)))
                .andExpect(status().isCreated());

        // Validate the FamilyNote in the database
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeCreate + 1);
        FamilyNote testFamilyNote = familyNotes.get(familyNotes.size() - 1);
        assertThat(testFamilyNote.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFamilyNote.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testFamilyNote.isArchived()).isEqualTo(DEFAULT_ARCHIVED);

        // Validate the FamilyNote in ElasticSearch
        FamilyNote familyNoteEs = familyNoteSearchRepository.findOne(testFamilyNote.getId());
        assertThat(familyNoteEs).isEqualToComparingFieldByField(testFamilyNote);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyNoteRepository.findAll().size();
        // set the field null
        familyNote.setContent(null);

        // Create the FamilyNote, which fails.

        restFamilyNoteMockMvc.perform(post("/api/family-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNote)))
                .andExpect(status().isBadRequest());

        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArchivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyNoteRepository.findAll().size();
        // set the field null
        familyNote.setArchived(null);

        // Create the FamilyNote, which fails.

        restFamilyNoteMockMvc.perform(post("/api/family-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNote)))
                .andExpect(status().isBadRequest());

        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilyNotes() throws Exception {
        // Initialize the database
        familyNoteRepository.saveAndFlush(familyNote);

        // Get all the familyNotes
        restFamilyNoteMockMvc.perform(get("/api/family-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(familyNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
                .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFamilyNote() throws Exception {
        // Initialize the database
        familyNoteRepository.saveAndFlush(familyNote);

        // Get the familyNote
        restFamilyNoteMockMvc.perform(get("/api/family-notes/{id}", familyNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(familyNote.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFamilyNote() throws Exception {
        // Get the familyNote
        restFamilyNoteMockMvc.perform(get("/api/family-notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilyNote() throws Exception {
        // Initialize the database
        familyNoteService.save((long) 1, familyNote);

        int databaseSizeBeforeUpdate = familyNoteRepository.findAll().size();

        // Update the familyNote
        FamilyNote updatedFamilyNote = new FamilyNote();
        updatedFamilyNote.setId(familyNote.getId());
        updatedFamilyNote.setContent(UPDATED_CONTENT);
        updatedFamilyNote.setTime(UPDATED_TIME);
        updatedFamilyNote.setArchived(UPDATED_ARCHIVED);

        restFamilyNoteMockMvc.perform(put("/api/family-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFamilyNote)))
                .andExpect(status().isOk());

        // Validate the FamilyNote in the database
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeUpdate);
        FamilyNote testFamilyNote = familyNotes.get(familyNotes.size() - 1);
        assertThat(testFamilyNote.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFamilyNote.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testFamilyNote.isArchived()).isEqualTo(UPDATED_ARCHIVED);

        // Validate the FamilyNote in ElasticSearch
        FamilyNote familyNoteEs = familyNoteSearchRepository.findOne(testFamilyNote.getId());
        assertThat(familyNoteEs).isEqualToComparingFieldByField(testFamilyNote);
    }

    @Test
    @Transactional
    public void deleteFamilyNote() throws Exception {
        // Initialize the database
        familyNoteService.save((long) 1, familyNote);

        int databaseSizeBeforeDelete = familyNoteRepository.findAll().size();

        // Get the familyNote
        restFamilyNoteMockMvc.perform(delete("/api/family-notes/{id}", familyNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean familyNoteExistsInEs = familyNoteSearchRepository.exists(familyNote.getId());
        assertThat(familyNoteExistsInEs).isFalse();

        // Validate the database is empty
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFamilyNote() throws Exception {
        // Initialize the database
        familyNoteService.save((long) 1, familyNote);

        // Search the familyNote
        restFamilyNoteMockMvc.perform(get("/api/_search/family-notes?query=id:" + familyNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())));
    }
}
