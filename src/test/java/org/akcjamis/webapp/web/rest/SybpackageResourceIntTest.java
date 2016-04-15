package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Sybpackage;
import org.akcjamis.webapp.repository.SybpackageRepository;
import org.akcjamis.webapp.repository.search.SybpackageSearchRepository;

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
 * Test class for the SybpackageResource REST controller.
 *
 * @see SybpackageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class SybpackageResourceIntTest {


    private static final Integer DEFAULT_SBK_ID = 1;
    private static final Integer UPDATED_SBK_ID = 2;

    @Inject
    private SybpackageRepository sybpackageRepository;

    @Inject
    private SybpackageSearchRepository sybpackageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSybpackageMockMvc;

    private Sybpackage sybpackage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SybpackageResource sybpackageResource = new SybpackageResource();
        ReflectionTestUtils.setField(sybpackageResource, "sybpackageSearchRepository", sybpackageSearchRepository);
        ReflectionTestUtils.setField(sybpackageResource, "sybpackageRepository", sybpackageRepository);
        this.restSybpackageMockMvc = MockMvcBuilders.standaloneSetup(sybpackageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sybpackageSearchRepository.deleteAll();
        sybpackage = new Sybpackage();
        sybpackage.setSbkID(DEFAULT_SBK_ID);
    }

    @Test
    @Transactional
    public void createSybpackage() throws Exception {
        int databaseSizeBeforeCreate = sybpackageRepository.findAll().size();

        // Create the Sybpackage

        restSybpackageMockMvc.perform(post("/api/sybpackages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sybpackage)))
                .andExpect(status().isCreated());

        // Validate the Sybpackage in the database
        List<Sybpackage> sybpackages = sybpackageRepository.findAll();
        assertThat(sybpackages).hasSize(databaseSizeBeforeCreate + 1);
        Sybpackage testSybpackage = sybpackages.get(sybpackages.size() - 1);
        assertThat(testSybpackage.getSbkID()).isEqualTo(DEFAULT_SBK_ID);

        // Validate the Sybpackage in ElasticSearch
        Sybpackage sybpackageEs = sybpackageSearchRepository.findOne(testSybpackage.getId());
        assertThat(sybpackageEs).isEqualToComparingFieldByField(testSybpackage);
    }

    @Test
    @Transactional
    public void getAllSybpackages() throws Exception {
        // Initialize the database
        sybpackageRepository.saveAndFlush(sybpackage);

        // Get all the sybpackages
        restSybpackageMockMvc.perform(get("/api/sybpackages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sybpackage.getId().intValue())))
                .andExpect(jsonPath("$.[*].sbkID").value(hasItem(DEFAULT_SBK_ID)));
    }

    @Test
    @Transactional
    public void getSybpackage() throws Exception {
        // Initialize the database
        sybpackageRepository.saveAndFlush(sybpackage);

        // Get the sybpackage
        restSybpackageMockMvc.perform(get("/api/sybpackages/{id}", sybpackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sybpackage.getId().intValue()))
            .andExpect(jsonPath("$.sbkID").value(DEFAULT_SBK_ID));
    }

    @Test
    @Transactional
    public void getNonExistingSybpackage() throws Exception {
        // Get the sybpackage
        restSybpackageMockMvc.perform(get("/api/sybpackages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSybpackage() throws Exception {
        // Initialize the database
        sybpackageRepository.saveAndFlush(sybpackage);
        sybpackageSearchRepository.save(sybpackage);
        int databaseSizeBeforeUpdate = sybpackageRepository.findAll().size();

        // Update the sybpackage
        Sybpackage updatedSybpackage = new Sybpackage();
        updatedSybpackage.setId(sybpackage.getId());
        updatedSybpackage.setSbkID(UPDATED_SBK_ID);

        restSybpackageMockMvc.perform(put("/api/sybpackages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSybpackage)))
                .andExpect(status().isOk());

        // Validate the Sybpackage in the database
        List<Sybpackage> sybpackages = sybpackageRepository.findAll();
        assertThat(sybpackages).hasSize(databaseSizeBeforeUpdate);
        Sybpackage testSybpackage = sybpackages.get(sybpackages.size() - 1);
        assertThat(testSybpackage.getSbkID()).isEqualTo(UPDATED_SBK_ID);

        // Validate the Sybpackage in ElasticSearch
        Sybpackage sybpackageEs = sybpackageSearchRepository.findOne(testSybpackage.getId());
        assertThat(sybpackageEs).isEqualToComparingFieldByField(testSybpackage);
    }

    @Test
    @Transactional
    public void deleteSybpackage() throws Exception {
        // Initialize the database
        sybpackageRepository.saveAndFlush(sybpackage);
        sybpackageSearchRepository.save(sybpackage);
        int databaseSizeBeforeDelete = sybpackageRepository.findAll().size();

        // Get the sybpackage
        restSybpackageMockMvc.perform(delete("/api/sybpackages/{id}", sybpackage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean sybpackageExistsInEs = sybpackageSearchRepository.exists(sybpackage.getId());
        assertThat(sybpackageExistsInEs).isFalse();

        // Validate the database is empty
        List<Sybpackage> sybpackages = sybpackageRepository.findAll();
        assertThat(sybpackages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSybpackage() throws Exception {
        // Initialize the database
        sybpackageRepository.saveAndFlush(sybpackage);
        sybpackageSearchRepository.save(sybpackage);

        // Search the sybpackage
        restSybpackageMockMvc.perform(get("/api/_search/sybpackages?query=id:" + sybpackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sybpackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sbkID").value(hasItem(DEFAULT_SBK_ID)));
    }
}
