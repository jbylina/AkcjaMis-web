package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.PackageNote;
import org.akcjamis.webapp.repository.PackageNoteRepository;
import org.akcjamis.webapp.repository.search.PackageNoteSearchRepository;

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
 * Test class for the PackageNoteResource REST controller.
 *
 * @see PackageNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class PackageNoteResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private PackageNoteRepository packageNoteRepository;

    @Inject
    private PackageNoteSearchRepository packageNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPackageNoteMockMvc;

    private PackageNote packageNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PackageNoteResource packageNoteResource = new PackageNoteResource();
        ReflectionTestUtils.setField(packageNoteResource, "packageNoteSearchRepository", packageNoteSearchRepository);
        ReflectionTestUtils.setField(packageNoteResource, "packageNoteRepository", packageNoteRepository);
        this.restPackageNoteMockMvc = MockMvcBuilders.standaloneSetup(packageNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        packageNoteSearchRepository.deleteAll();
        packageNote = new PackageNote();
        packageNote.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createPackageNote() throws Exception {
        int databaseSizeBeforeCreate = packageNoteRepository.findAll().size();

        // Create the PackageNote

        restPackageNoteMockMvc.perform(post("/api/package-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(packageNote)))
                .andExpect(status().isCreated());

        // Validate the PackageNote in the database
        List<PackageNote> packageNotes = packageNoteRepository.findAll();
        assertThat(packageNotes).hasSize(databaseSizeBeforeCreate + 1);
        PackageNote testPackageNote = packageNotes.get(packageNotes.size() - 1);
        assertThat(testPackageNote.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the PackageNote in ElasticSearch
        PackageNote packageNoteEs = packageNoteSearchRepository.findOne(testPackageNote.getId());
        assertThat(packageNoteEs).isEqualToComparingFieldByField(testPackageNote);
    }

    @Test
    @Transactional
    public void getAllPackageNotes() throws Exception {
        // Initialize the database
        packageNoteRepository.saveAndFlush(packageNote);

        // Get all the packageNotes
        restPackageNoteMockMvc.perform(get("/api/package-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(packageNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getPackageNote() throws Exception {
        // Initialize the database
        packageNoteRepository.saveAndFlush(packageNote);

        // Get the packageNote
        restPackageNoteMockMvc.perform(get("/api/package-notes/{id}", packageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(packageNote.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPackageNote() throws Exception {
        // Get the packageNote
        restPackageNoteMockMvc.perform(get("/api/package-notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageNote() throws Exception {
        // Initialize the database
        packageNoteRepository.saveAndFlush(packageNote);
        packageNoteSearchRepository.save(packageNote);
        int databaseSizeBeforeUpdate = packageNoteRepository.findAll().size();

        // Update the packageNote
        PackageNote updatedPackageNote = new PackageNote();
        updatedPackageNote.setId(packageNote.getId());
        updatedPackageNote.setText(UPDATED_TEXT);

        restPackageNoteMockMvc.perform(put("/api/package-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPackageNote)))
                .andExpect(status().isOk());

        // Validate the PackageNote in the database
        List<PackageNote> packageNotes = packageNoteRepository.findAll();
        assertThat(packageNotes).hasSize(databaseSizeBeforeUpdate);
        PackageNote testPackageNote = packageNotes.get(packageNotes.size() - 1);
        assertThat(testPackageNote.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the PackageNote in ElasticSearch
        PackageNote packageNoteEs = packageNoteSearchRepository.findOne(testPackageNote.getId());
        assertThat(packageNoteEs).isEqualToComparingFieldByField(testPackageNote);
    }

    @Test
    @Transactional
    public void deletePackageNote() throws Exception {
        // Initialize the database
        packageNoteRepository.saveAndFlush(packageNote);
        packageNoteSearchRepository.save(packageNote);
        int databaseSizeBeforeDelete = packageNoteRepository.findAll().size();

        // Get the packageNote
        restPackageNoteMockMvc.perform(delete("/api/package-notes/{id}", packageNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean packageNoteExistsInEs = packageNoteSearchRepository.exists(packageNote.getId());
        assertThat(packageNoteExistsInEs).isFalse();

        // Validate the database is empty
        List<PackageNote> packageNotes = packageNoteRepository.findAll();
        assertThat(packageNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackageNote() throws Exception {
        // Initialize the database
        packageNoteRepository.saveAndFlush(packageNote);
        packageNoteSearchRepository.save(packageNote);

        // Search the packageNote
        restPackageNoteMockMvc.perform(get("/api/_search/package-notes?query=id:" + packageNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }
}
