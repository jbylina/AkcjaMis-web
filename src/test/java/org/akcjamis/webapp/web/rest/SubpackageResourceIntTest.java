package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Subpackage;
import org.akcjamis.webapp.repository.SubpackageRepository;

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
 * Test class for the SubpackageResource REST controller.
 *
 * @see SubpackageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class SubpackageResourceIntTest {


    private static final Integer DEFAULT_SUBPACKAGE_NUMBER = 1;
    private static final Integer UPDATED_SUBPACKAGE_NUMBER = 2;

    @Inject
    private SubpackageRepository subpackageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSubpackageMockMvc;

    private Subpackage subpackage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubpackageResource subpackageResource = new SubpackageResource();
        ReflectionTestUtils.setField(subpackageResource, "subpackageRepository", subpackageRepository);
        this.restSubpackageMockMvc = MockMvcBuilders.standaloneSetup(subpackageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        subpackage = new Subpackage();
        subpackage.setSubpackageNumber(DEFAULT_SUBPACKAGE_NUMBER);
    }

    @Test
    @Transactional
    public void createSubpackage() throws Exception {
        int databaseSizeBeforeCreate = subpackageRepository.findAll().size();

        // Create the Subpackage

        restSubpackageMockMvc.perform(post("/api/subpackages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subpackage)))
                .andExpect(status().isCreated());

        // Validate the Subpackage in the database
        List<Subpackage> subpackages = subpackageRepository.findAll();
        assertThat(subpackages).hasSize(databaseSizeBeforeCreate + 1);
        Subpackage testSubpackage = subpackages.get(subpackages.size() - 1);
        assertThat(testSubpackage.getSubpackageNumber()).isEqualTo(DEFAULT_SUBPACKAGE_NUMBER);
    }

    @Test
    @Transactional
    public void checkSubpackageNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = subpackageRepository.findAll().size();
        // set the field null
        subpackage.setSubpackageNumber(null);

        // Create the Subpackage, which fails.

        restSubpackageMockMvc.perform(post("/api/subpackages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subpackage)))
                .andExpect(status().isBadRequest());

        List<Subpackage> subpackages = subpackageRepository.findAll();
        assertThat(subpackages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubpackages() throws Exception {
        // Initialize the database
        subpackageRepository.saveAndFlush(subpackage);

        // Get all the subpackages
        restSubpackageMockMvc.perform(get("/api/subpackages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subpackage.getId().intValue())))
                .andExpect(jsonPath("$.[*].subpackageNumber").value(hasItem(DEFAULT_SUBPACKAGE_NUMBER)));
    }

    @Test
    @Transactional
    public void getSubpackage() throws Exception {
        // Initialize the database
        subpackageRepository.saveAndFlush(subpackage);

        // Get the subpackage
        restSubpackageMockMvc.perform(get("/api/subpackages/{id}", subpackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(subpackage.getId().intValue()))
            .andExpect(jsonPath("$.subpackageNumber").value(DEFAULT_SUBPACKAGE_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingSubpackage() throws Exception {
        // Get the subpackage
        restSubpackageMockMvc.perform(get("/api/subpackages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubpackage() throws Exception {
        // Initialize the database
        subpackageRepository.saveAndFlush(subpackage);
        int databaseSizeBeforeUpdate = subpackageRepository.findAll().size();

        // Update the subpackage
        Subpackage updatedSubpackage = new Subpackage();
        updatedSubpackage.setId(subpackage.getId());
        updatedSubpackage.setSubpackageNumber(UPDATED_SUBPACKAGE_NUMBER);

        restSubpackageMockMvc.perform(put("/api/subpackages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubpackage)))
                .andExpect(status().isOk());

        // Validate the Subpackage in the database
        List<Subpackage> subpackages = subpackageRepository.findAll();
        assertThat(subpackages).hasSize(databaseSizeBeforeUpdate);
        Subpackage testSubpackage = subpackages.get(subpackages.size() - 1);
        assertThat(testSubpackage.getSubpackageNumber()).isEqualTo(UPDATED_SUBPACKAGE_NUMBER);
    }

    @Test
    @Transactional
    public void deleteSubpackage() throws Exception {
        // Initialize the database
        subpackageRepository.saveAndFlush(subpackage);
        int databaseSizeBeforeDelete = subpackageRepository.findAll().size();

        // Get the subpackage
        restSubpackageMockMvc.perform(delete("/api/subpackages/{id}", subpackage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Subpackage> subpackages = subpackageRepository.findAll();
        assertThat(subpackages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
