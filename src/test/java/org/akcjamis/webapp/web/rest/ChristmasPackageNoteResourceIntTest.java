package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.akcjamis.webapp.repository.ChristmasPackageNoteRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageNoteSearchRepository;

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

    private static final String DEFAULT_CONTENT = "AAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBB";

    @Inject
    private ChristmasPackageNoteRepository christmasPackageNoteRepository;

    @Inject
    private ChristmasPackageNoteSearchRepository christmasPackageNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChristmasPackageNoteMockMvc;

    private ChristmasPackageNote christmasPackageNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageNoteResource christmasPackageNoteResource = new ChristmasPackageNoteResource();
        ReflectionTestUtils.setField(christmasPackageNoteResource, "christmasPackageNoteSearchRepository", christmasPackageNoteSearchRepository);
        ReflectionTestUtils.setField(christmasPackageNoteResource, "christmasPackageNoteRepository", christmasPackageNoteRepository);
        this.restChristmasPackageNoteMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        christmasPackageNoteSearchRepository.deleteAll();
        christmasPackageNote = new ChristmasPackageNote();
        christmasPackageNote.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createChristmasPackageNote() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageNoteRepository.findAll().size();

        // Create the ChristmasPackageNote

        restChristmasPackageNoteMockMvc.perform(post("/api/christmas-package-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageNote)))
                .andExpect(status().isCreated());

        // Validate the ChristmasPackageNote in the database
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeCreate + 1);
        ChristmasPackageNote testChristmasPackageNote = christmasPackageNotes.get(christmasPackageNotes.size() - 1);
        assertThat(testChristmasPackageNote.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the ChristmasPackageNote in ElasticSearch
        ChristmasPackageNote christmasPackageNoteEs = christmasPackageNoteSearchRepository.findOne(testChristmasPackageNote.getId());
        assertThat(christmasPackageNoteEs).isEqualToComparingFieldByField(testChristmasPackageNote);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageNoteRepository.findAll().size();
        // set the field null
        christmasPackageNote.setContent(null);

        // Create the ChristmasPackageNote, which fails.

        restChristmasPackageNoteMockMvc.perform(post("/api/christmas-package-notes")
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
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);

        // Get all the christmasPackageNotes
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-package-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);

        // Get the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-package-notes/{id}", christmasPackageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackageNote.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackageNote() throws Exception {
        // Get the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(get("/api/christmas-package-notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);
        christmasPackageNoteSearchRepository.save(christmasPackageNote);
        int databaseSizeBeforeUpdate = christmasPackageNoteRepository.findAll().size();

        // Update the christmasPackageNote
        ChristmasPackageNote updatedChristmasPackageNote = new ChristmasPackageNote();
        updatedChristmasPackageNote.setId(christmasPackageNote.getId());
        updatedChristmasPackageNote.setContent(UPDATED_CONTENT);

        restChristmasPackageNoteMockMvc.perform(put("/api/christmas-package-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChristmasPackageNote)))
                .andExpect(status().isOk());

        // Validate the ChristmasPackageNote in the database
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeUpdate);
        ChristmasPackageNote testChristmasPackageNote = christmasPackageNotes.get(christmasPackageNotes.size() - 1);
        assertThat(testChristmasPackageNote.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the ChristmasPackageNote in ElasticSearch
        ChristmasPackageNote christmasPackageNoteEs = christmasPackageNoteSearchRepository.findOne(testChristmasPackageNote.getId());
        assertThat(christmasPackageNoteEs).isEqualToComparingFieldByField(testChristmasPackageNote);
    }

    @Test
    @Transactional
    public void deleteChristmasPackageNote() throws Exception {
        // Initialize the database
        christmasPackageNoteRepository.saveAndFlush(christmasPackageNote);
        christmasPackageNoteSearchRepository.save(christmasPackageNote);
        int databaseSizeBeforeDelete = christmasPackageNoteRepository.findAll().size();

        // Get the christmasPackageNote
        restChristmasPackageNoteMockMvc.perform(delete("/api/christmas-package-notes/{id}", christmasPackageNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean christmasPackageNoteExistsInEs = christmasPackageNoteSearchRepository.exists(christmasPackageNote.getId());
        assertThat(christmasPackageNoteExistsInEs).isFalse();

        // Validate the database is empty
        List<ChristmasPackageNote> christmasPackageNotes = christmasPackageNoteRepository.findAll();
        assertThat(christmasPackageNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChristmasPackageNote() throws Exception {
        // Initialize the database
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
