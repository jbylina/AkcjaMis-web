package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.service.ChristmasPackageService;
import org.akcjamis.webapp.repository.search.ChristmasPackageSearchRepository;

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
 * Test class for the ChristmasPackageResource REST controller.
 *
 * @see ChristmasPackageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChristmasPackageResourceIntTest {


    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final Boolean DEFAULT_DELIVERED = false;
    private static final Boolean UPDATED_DELIVERED = true;

    private static final Integer DEFAULT_PACKAGE_NUMBER = 1;
    private static final Integer UPDATED_PACKAGE_NUMBER = 2;

    @Inject
    private ChristmasPackageRepository christmasPackageRepository;

    @Inject
    private ChristmasPackageService christmasPackageService;

    @Inject
    private ChristmasPackageSearchRepository christmasPackageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChristmasPackageMockMvc;

    private ChristmasPackage christmasPackage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageResource christmasPackageResource = new ChristmasPackageResource(christmasPackageService);
        ReflectionTestUtils.setField(christmasPackageResource, "christmasPackageService", christmasPackageService);
        this.restChristmasPackageMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        christmasPackageSearchRepository.deleteAll();
        christmasPackage = new ChristmasPackage();
        christmasPackage.setMark(DEFAULT_MARK);
        christmasPackage.setDelivered(DEFAULT_DELIVERED);
        christmasPackage.setPackageNumber(DEFAULT_PACKAGE_NUMBER);
    }

    @Test
    @Transactional
    public void createChristmasPackage() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageRepository.findAll().size();

        // Create the ChristmasPackage

        restChristmasPackageMockMvc.perform(post("/api/christmas-packages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackage)))
                .andExpect(status().isCreated());

        // Validate the ChristmasPackage in the database
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeCreate + 1);
        ChristmasPackage testChristmasPackage = christmasPackages.get(christmasPackages.size() - 1);
        assertThat(testChristmasPackage.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testChristmasPackage.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testChristmasPackage.getPackageNumber()).isEqualTo(DEFAULT_PACKAGE_NUMBER);

        // Validate the ChristmasPackage in ElasticSearch
        ChristmasPackage christmasPackageEs = christmasPackageSearchRepository.findOne(testChristmasPackage.getId());
        assertThat(christmasPackageEs).isEqualToComparingFieldByField(testChristmasPackage);
    }

    @Test
    @Transactional
    public void checkDeliveredIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageRepository.findAll().size();
        // set the field null
        christmasPackage.setDelivered(null);

        // Create the ChristmasPackage, which fails.

        restChristmasPackageMockMvc.perform(post("/api/christmas-packages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackage)))
                .andExpect(status().isBadRequest());

        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPackageNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageRepository.findAll().size();
        // set the field null
        christmasPackage.setPackageNumber(null);

        // Create the ChristmasPackage, which fails.

        restChristmasPackageMockMvc.perform(post("/api/christmas-packages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackage)))
                .andExpect(status().isBadRequest());

        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChristmasPackages() throws Exception {
        // Initialize the database
        christmasPackageRepository.saveAndFlush(christmasPackage);

        // Get all the christmasPackages
        restChristmasPackageMockMvc.perform(get("/api/christmas-packages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackage.getId().intValue())))
                .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
                .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
                .andExpect(jsonPath("$.[*].packageNumber").value(hasItem(DEFAULT_PACKAGE_NUMBER)));
    }

    @Test
    @Transactional
    public void getChristmasPackage() throws Exception {
        // Initialize the database
        christmasPackageRepository.saveAndFlush(christmasPackage);

        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/christmas-packages/{id}", christmasPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackage.getId().intValue()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED.booleanValue()))
            .andExpect(jsonPath("$.packageNumber").value(DEFAULT_PACKAGE_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackage() throws Exception {
        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/christmas-packages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackage() throws Exception {
        // Initialize the database
        christmasPackageService.save((long) 1, christmasPackage);

        int databaseSizeBeforeUpdate = christmasPackageRepository.findAll().size();

        // Update the christmasPackage
        ChristmasPackage updatedChristmasPackage = new ChristmasPackage();
        updatedChristmasPackage.setId(christmasPackage.getId());
        updatedChristmasPackage.setMark(UPDATED_MARK);
        updatedChristmasPackage.setDelivered(UPDATED_DELIVERED);
        updatedChristmasPackage.setPackageNumber(UPDATED_PACKAGE_NUMBER);

        restChristmasPackageMockMvc.perform(put("/api/christmas-packages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChristmasPackage)))
                .andExpect(status().isOk());

        // Validate the ChristmasPackage in the database
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeUpdate);
        ChristmasPackage testChristmasPackage = christmasPackages.get(christmasPackages.size() - 1);
        assertThat(testChristmasPackage.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testChristmasPackage.isDelivered()).isEqualTo(UPDATED_DELIVERED);
        assertThat(testChristmasPackage.getPackageNumber()).isEqualTo(UPDATED_PACKAGE_NUMBER);

        // Validate the ChristmasPackage in ElasticSearch
        ChristmasPackage christmasPackageEs = christmasPackageSearchRepository.findOne(testChristmasPackage.getId());
        assertThat(christmasPackageEs).isEqualToComparingFieldByField(testChristmasPackage);
    }

    @Test
    @Transactional
    public void deleteChristmasPackage() throws Exception {
        // Initialize the database
        christmasPackageService.save((long) 1, christmasPackage);

        int databaseSizeBeforeDelete = christmasPackageRepository.findAll().size();

        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(delete("/api/christmas-packages/{id}", christmasPackage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean christmasPackageExistsInEs = christmasPackageSearchRepository.exists(christmasPackage.getId());
        assertThat(christmasPackageExistsInEs).isFalse();

        // Validate the database is empty
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChristmasPackage() throws Exception {
        // Initialize the database
        christmasPackageService.save((long) 1, christmasPackage);

        // Search the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/_search/christmas-packages?query=id:" + christmasPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].packageNumber").value(hasItem(DEFAULT_PACKAGE_NUMBER)));
    }
}
