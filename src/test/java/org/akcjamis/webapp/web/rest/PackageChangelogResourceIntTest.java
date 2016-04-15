package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.PackageChangelog;
import org.akcjamis.webapp.repository.PackageChangelogRepository;
import org.akcjamis.webapp.repository.search.PackageChangelogSearchRepository;

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
 * Test class for the PackageChangelogResource REST controller.
 *
 * @see PackageChangelogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class PackageChangelogResourceIntTest {

    private static final String DEFAULT_TYPE_CODE = "AAAAA";
    private static final String UPDATED_TYPE_CODE = "BBBBB";

    private static final LocalDate DEFAULT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private PackageChangelogRepository packageChangelogRepository;

    @Inject
    private PackageChangelogSearchRepository packageChangelogSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPackageChangelogMockMvc;

    private PackageChangelog packageChangelog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PackageChangelogResource packageChangelogResource = new PackageChangelogResource();
        ReflectionTestUtils.setField(packageChangelogResource, "packageChangelogSearchRepository", packageChangelogSearchRepository);
        ReflectionTestUtils.setField(packageChangelogResource, "packageChangelogRepository", packageChangelogRepository);
        this.restPackageChangelogMockMvc = MockMvcBuilders.standaloneSetup(packageChangelogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        packageChangelogSearchRepository.deleteAll();
        packageChangelog = new PackageChangelog();
        packageChangelog.setTypeCode(DEFAULT_TYPE_CODE);
        packageChangelog.setTime(DEFAULT_TIME);
        packageChangelog.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createPackageChangelog() throws Exception {
        int databaseSizeBeforeCreate = packageChangelogRepository.findAll().size();

        // Create the PackageChangelog

        restPackageChangelogMockMvc.perform(post("/api/package-changelogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(packageChangelog)))
                .andExpect(status().isCreated());

        // Validate the PackageChangelog in the database
        List<PackageChangelog> packageChangelogs = packageChangelogRepository.findAll();
        assertThat(packageChangelogs).hasSize(databaseSizeBeforeCreate + 1);
        PackageChangelog testPackageChangelog = packageChangelogs.get(packageChangelogs.size() - 1);
        assertThat(testPackageChangelog.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testPackageChangelog.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testPackageChangelog.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the PackageChangelog in ElasticSearch
        PackageChangelog packageChangelogEs = packageChangelogSearchRepository.findOne(testPackageChangelog.getId());
        assertThat(packageChangelogEs).isEqualToComparingFieldByField(testPackageChangelog);
    }

    @Test
    @Transactional
    public void getAllPackageChangelogs() throws Exception {
        // Initialize the database
        packageChangelogRepository.saveAndFlush(packageChangelog);

        // Get all the packageChangelogs
        restPackageChangelogMockMvc.perform(get("/api/package-changelogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(packageChangelog.getId().intValue())))
                .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getPackageChangelog() throws Exception {
        // Initialize the database
        packageChangelogRepository.saveAndFlush(packageChangelog);

        // Get the packageChangelog
        restPackageChangelogMockMvc.perform(get("/api/package-changelogs/{id}", packageChangelog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(packageChangelog.getId().intValue()))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPackageChangelog() throws Exception {
        // Get the packageChangelog
        restPackageChangelogMockMvc.perform(get("/api/package-changelogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageChangelog() throws Exception {
        // Initialize the database
        packageChangelogRepository.saveAndFlush(packageChangelog);
        packageChangelogSearchRepository.save(packageChangelog);
        int databaseSizeBeforeUpdate = packageChangelogRepository.findAll().size();

        // Update the packageChangelog
        PackageChangelog updatedPackageChangelog = new PackageChangelog();
        updatedPackageChangelog.setId(packageChangelog.getId());
        updatedPackageChangelog.setTypeCode(UPDATED_TYPE_CODE);
        updatedPackageChangelog.setTime(UPDATED_TIME);
        updatedPackageChangelog.setText(UPDATED_TEXT);

        restPackageChangelogMockMvc.perform(put("/api/package-changelogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPackageChangelog)))
                .andExpect(status().isOk());

        // Validate the PackageChangelog in the database
        List<PackageChangelog> packageChangelogs = packageChangelogRepository.findAll();
        assertThat(packageChangelogs).hasSize(databaseSizeBeforeUpdate);
        PackageChangelog testPackageChangelog = packageChangelogs.get(packageChangelogs.size() - 1);
        assertThat(testPackageChangelog.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testPackageChangelog.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testPackageChangelog.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the PackageChangelog in ElasticSearch
        PackageChangelog packageChangelogEs = packageChangelogSearchRepository.findOne(testPackageChangelog.getId());
        assertThat(packageChangelogEs).isEqualToComparingFieldByField(testPackageChangelog);
    }

    @Test
    @Transactional
    public void deletePackageChangelog() throws Exception {
        // Initialize the database
        packageChangelogRepository.saveAndFlush(packageChangelog);
        packageChangelogSearchRepository.save(packageChangelog);
        int databaseSizeBeforeDelete = packageChangelogRepository.findAll().size();

        // Get the packageChangelog
        restPackageChangelogMockMvc.perform(delete("/api/package-changelogs/{id}", packageChangelog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean packageChangelogExistsInEs = packageChangelogSearchRepository.exists(packageChangelog.getId());
        assertThat(packageChangelogExistsInEs).isFalse();

        // Validate the database is empty
        List<PackageChangelog> packageChangelogs = packageChangelogRepository.findAll();
        assertThat(packageChangelogs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackageChangelog() throws Exception {
        // Initialize the database
        packageChangelogRepository.saveAndFlush(packageChangelog);
        packageChangelogSearchRepository.save(packageChangelog);

        // Search the packageChangelog
        restPackageChangelogMockMvc.perform(get("/api/_search/package-changelogs?query=id:" + packageChangelog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageChangelog.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }
}
