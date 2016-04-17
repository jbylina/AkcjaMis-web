package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.ChristmasPackageChange;
import org.akcjamis.webapp.repository.ChristmasPackageChangeRepository;
import org.akcjamis.webapp.repository.search.ChristmasPackageChangeSearchRepository;

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
 * Test class for the ChristmasPackageChangeResource REST controller.
 *
 * @see ChristmasPackageChangeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChristmasPackageChangeResourceIntTest {

    private static final String DEFAULT_TYPE_CODE = "AAA";
    private static final String UPDATED_TYPE_CODE = "BBB";

    private static final LocalDate DEFAULT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CONTENT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ChristmasPackageChangeRepository christmasPackageChangeRepository;

    @Inject
    private ChristmasPackageChangeSearchRepository christmasPackageChangeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChristmasPackageChangeMockMvc;

    private ChristmasPackageChange christmasPackageChange;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageChangeResource christmasPackageChangeResource = new ChristmasPackageChangeResource();
        ReflectionTestUtils.setField(christmasPackageChangeResource, "christmasPackageChangeSearchRepository", christmasPackageChangeSearchRepository);
        ReflectionTestUtils.setField(christmasPackageChangeResource, "christmasPackageChangeRepository", christmasPackageChangeRepository);
        this.restChristmasPackageChangeMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageChangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        christmasPackageChangeSearchRepository.deleteAll();
        christmasPackageChange = new ChristmasPackageChange();
        christmasPackageChange.setTypeCode(DEFAULT_TYPE_CODE);
        christmasPackageChange.setTime(DEFAULT_TIME);
        christmasPackageChange.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createChristmasPackageChange() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageChangeRepository.findAll().size();

        // Create the ChristmasPackageChange

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageChange)))
                .andExpect(status().isCreated());

        // Validate the ChristmasPackageChange in the database
        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeCreate + 1);
        ChristmasPackageChange testChristmasPackageChange = christmasPackageChanges.get(christmasPackageChanges.size() - 1);
        assertThat(testChristmasPackageChange.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testChristmasPackageChange.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testChristmasPackageChange.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the ChristmasPackageChange in ElasticSearch
        ChristmasPackageChange christmasPackageChangeEs = christmasPackageChangeSearchRepository.findOne(testChristmasPackageChange.getId());
        assertThat(christmasPackageChangeEs).isEqualToComparingFieldByField(testChristmasPackageChange);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageChangeRepository.findAll().size();
        // set the field null
        christmasPackageChange.setTime(null);

        // Create the ChristmasPackageChange, which fails.

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageChange)))
                .andExpect(status().isBadRequest());

        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageChangeRepository.findAll().size();
        // set the field null
        christmasPackageChange.setContent(null);

        // Create the ChristmasPackageChange, which fails.

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageChange)))
                .andExpect(status().isBadRequest());

        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChristmasPackageChanges() throws Exception {
        // Initialize the database
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);

        // Get all the christmasPackageChanges
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package-changes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageChange.getId().intValue())))
                .andExpect(jsonPath("$.[*].type_code").value(hasItem(DEFAULT_TYPE_CODE.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);

        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package-changes/{id}", christmasPackageChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackageChange.getId().intValue()))
            .andExpect(jsonPath("$.type_code").value(DEFAULT_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackageChange() throws Exception {
        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package-changes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);
        christmasPackageChangeSearchRepository.save(christmasPackageChange);
        int databaseSizeBeforeUpdate = christmasPackageChangeRepository.findAll().size();

        // Update the christmasPackageChange
        ChristmasPackageChange updatedChristmasPackageChange = new ChristmasPackageChange();
        updatedChristmasPackageChange.setId(christmasPackageChange.getId());
        updatedChristmasPackageChange.setTypeCode(UPDATED_TYPE_CODE);
        updatedChristmasPackageChange.setTime(UPDATED_TIME);
        updatedChristmasPackageChange.setContent(UPDATED_CONTENT);

        restChristmasPackageChangeMockMvc.perform(put("/api/christmas-package-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChristmasPackageChange)))
                .andExpect(status().isOk());

        // Validate the ChristmasPackageChange in the database
        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeUpdate);
        ChristmasPackageChange testChristmasPackageChange = christmasPackageChanges.get(christmasPackageChanges.size() - 1);
        assertThat(testChristmasPackageChange.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testChristmasPackageChange.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testChristmasPackageChange.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the ChristmasPackageChange in ElasticSearch
        ChristmasPackageChange christmasPackageChangeEs = christmasPackageChangeSearchRepository.findOne(testChristmasPackageChange.getId());
        assertThat(christmasPackageChangeEs).isEqualToComparingFieldByField(testChristmasPackageChange);
    }

    @Test
    @Transactional
    public void deleteChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);
        christmasPackageChangeSearchRepository.save(christmasPackageChange);
        int databaseSizeBeforeDelete = christmasPackageChangeRepository.findAll().size();

        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(delete("/api/christmas-package-changes/{id}", christmasPackageChange.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean christmasPackageChangeExistsInEs = christmasPackageChangeSearchRepository.exists(christmasPackageChange.getId());
        assertThat(christmasPackageChangeExistsInEs).isFalse();

        // Validate the database is empty
        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);
        christmasPackageChangeSearchRepository.save(christmasPackageChange);

        // Search the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(get("/api/_search/christmas-package-changes?query=id:" + christmasPackageChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].type_code").value(hasItem(DEFAULT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
}
