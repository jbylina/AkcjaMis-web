package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.SubpackageNote;
import org.akcjamis.webapp.repository.SubpackageNoteRepository;

import org.junit.Before;
import org.junit.Ignore;
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
 * Test class for the SubpackageNoteResource REST controller.
 *
 * @see SubpackageNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
@Ignore("Not ready")
public class SubpackageNoteResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBB";

    @Inject
    private SubpackageNoteRepository subpackageNoteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSubpackageNoteMockMvc;

    private SubpackageNote subpackageNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubpackageNoteResource subpackageNoteResource = new SubpackageNoteResource();
        ReflectionTestUtils.setField(subpackageNoteResource, "subpackageNoteRepository", subpackageNoteRepository);
        this.restSubpackageNoteMockMvc = MockMvcBuilders.standaloneSetup(subpackageNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        subpackageNote = new SubpackageNote();
        subpackageNote.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createSubpackageNote() throws Exception {
        int databaseSizeBeforeCreate = subpackageNoteRepository.findAll().size();

        // Create the SubpackageNote

        restSubpackageNoteMockMvc.perform(post("/api/subpackage-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subpackageNote)))
                .andExpect(status().isCreated());

        // Validate the SubpackageNote in the database
        List<SubpackageNote> subpackageNotes = subpackageNoteRepository.findAll();
        assertThat(subpackageNotes).hasSize(databaseSizeBeforeCreate + 1);
        SubpackageNote testSubpackageNote = subpackageNotes.get(subpackageNotes.size() - 1);
        assertThat(testSubpackageNote.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = subpackageNoteRepository.findAll().size();
        // set the field null
        subpackageNote.setContent(null);

        // Create the SubpackageNote, which fails.

        restSubpackageNoteMockMvc.perform(post("/api/subpackage-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subpackageNote)))
                .andExpect(status().isBadRequest());

        List<SubpackageNote> subpackageNotes = subpackageNoteRepository.findAll();
        assertThat(subpackageNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubpackageNotes() throws Exception {
        // Initialize the database
        subpackageNoteRepository.saveAndFlush(subpackageNote);

        // Get all the subpackageNotes
        restSubpackageNoteMockMvc.perform(get("/api/subpackage-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subpackageNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getSubpackageNote() throws Exception {
        // Initialize the database
        subpackageNoteRepository.saveAndFlush(subpackageNote);

        // Get the subpackageNote
        restSubpackageNoteMockMvc.perform(get("/api/subpackage-notes/{id}", subpackageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(subpackageNote.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubpackageNote() throws Exception {
        // Get the subpackageNote
        restSubpackageNoteMockMvc.perform(get("/api/subpackage-notes/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubpackageNote() throws Exception {
        // Initialize the database
        subpackageNoteRepository.saveAndFlush(subpackageNote);
        int databaseSizeBeforeUpdate = subpackageNoteRepository.findAll().size();

        // Update the subpackageNote
        SubpackageNote updatedSubpackageNote = new SubpackageNote();
        updatedSubpackageNote.setId(subpackageNote.getId());
        updatedSubpackageNote.setContent(UPDATED_CONTENT);

        restSubpackageNoteMockMvc.perform(put("/api/subpackage-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubpackageNote)))
                .andExpect(status().isOk());

        // Validate the SubpackageNote in the database
        List<SubpackageNote> subpackageNotes = subpackageNoteRepository.findAll();
        assertThat(subpackageNotes).hasSize(databaseSizeBeforeUpdate);
        SubpackageNote testSubpackageNote = subpackageNotes.get(subpackageNotes.size() - 1);
        assertThat(testSubpackageNote.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteSubpackageNote() throws Exception {
        // Initialize the database
        subpackageNoteRepository.saveAndFlush(subpackageNote);
        int databaseSizeBeforeDelete = subpackageNoteRepository.findAll().size();

        // Get the subpackageNote
        restSubpackageNoteMockMvc.perform(delete("/api/subpackage-notes/{id}", subpackageNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SubpackageNote> subpackageNotes = subpackageNoteRepository.findAll();
        assertThat(subpackageNotes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
