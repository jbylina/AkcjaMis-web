package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.PackageGift;
import org.akcjamis.webapp.repository.PackageGiftRepository;
import org.akcjamis.webapp.repository.search.PackageGiftSearchRepository;

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
 * Test class for the PackageGiftResource REST controller.
 *
 * @see PackageGiftResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class PackageGiftResourceIntTest {


    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final Integer DEFAULT_DELIVERED = 1;
    private static final Integer UPDATED_DELIVERED = 2;

    @Inject
    private PackageGiftRepository packageGiftRepository;

    @Inject
    private PackageGiftSearchRepository packageGiftSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPackageGiftMockMvc;

    private PackageGift packageGift;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PackageGiftResource packageGiftResource = new PackageGiftResource();
        ReflectionTestUtils.setField(packageGiftResource, "packageGiftSearchRepository", packageGiftSearchRepository);
        ReflectionTestUtils.setField(packageGiftResource, "packageGiftRepository", packageGiftRepository);
        this.restPackageGiftMockMvc = MockMvcBuilders.standaloneSetup(packageGiftResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        packageGiftSearchRepository.deleteAll();
        packageGift = new PackageGift();
        packageGift.setMark(DEFAULT_MARK);
        packageGift.setDelivered(DEFAULT_DELIVERED);
    }

    @Test
    @Transactional
    public void createPackageGift() throws Exception {
        int databaseSizeBeforeCreate = packageGiftRepository.findAll().size();

        // Create the PackageGift

        restPackageGiftMockMvc.perform(post("/api/package-gifts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(packageGift)))
                .andExpect(status().isCreated());

        // Validate the PackageGift in the database
        List<PackageGift> packageGifts = packageGiftRepository.findAll();
        assertThat(packageGifts).hasSize(databaseSizeBeforeCreate + 1);
        PackageGift testPackageGift = packageGifts.get(packageGifts.size() - 1);
        assertThat(testPackageGift.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testPackageGift.getDelivered()).isEqualTo(DEFAULT_DELIVERED);

        // Validate the PackageGift in ElasticSearch
        PackageGift packageGiftEs = packageGiftSearchRepository.findOne(testPackageGift.getId());
        assertThat(packageGiftEs).isEqualToComparingFieldByField(testPackageGift);
    }

    @Test
    @Transactional
    public void getAllPackageGifts() throws Exception {
        // Initialize the database
        packageGiftRepository.saveAndFlush(packageGift);

        // Get all the packageGifts
        restPackageGiftMockMvc.perform(get("/api/package-gifts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(packageGift.getId().intValue())))
                .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
                .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)));
    }

    @Test
    @Transactional
    public void getPackageGift() throws Exception {
        // Initialize the database
        packageGiftRepository.saveAndFlush(packageGift);

        // Get the packageGift
        restPackageGiftMockMvc.perform(get("/api/package-gifts/{id}", packageGift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(packageGift.getId().intValue()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED));
    }

    @Test
    @Transactional
    public void getNonExistingPackageGift() throws Exception {
        // Get the packageGift
        restPackageGiftMockMvc.perform(get("/api/package-gifts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageGift() throws Exception {
        // Initialize the database
        packageGiftRepository.saveAndFlush(packageGift);
        packageGiftSearchRepository.save(packageGift);
        int databaseSizeBeforeUpdate = packageGiftRepository.findAll().size();

        // Update the packageGift
        PackageGift updatedPackageGift = new PackageGift();
        updatedPackageGift.setId(packageGift.getId());
        updatedPackageGift.setMark(UPDATED_MARK);
        updatedPackageGift.setDelivered(UPDATED_DELIVERED);

        restPackageGiftMockMvc.perform(put("/api/package-gifts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPackageGift)))
                .andExpect(status().isOk());

        // Validate the PackageGift in the database
        List<PackageGift> packageGifts = packageGiftRepository.findAll();
        assertThat(packageGifts).hasSize(databaseSizeBeforeUpdate);
        PackageGift testPackageGift = packageGifts.get(packageGifts.size() - 1);
        assertThat(testPackageGift.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testPackageGift.getDelivered()).isEqualTo(UPDATED_DELIVERED);

        // Validate the PackageGift in ElasticSearch
        PackageGift packageGiftEs = packageGiftSearchRepository.findOne(testPackageGift.getId());
        assertThat(packageGiftEs).isEqualToComparingFieldByField(testPackageGift);
    }

    @Test
    @Transactional
    public void deletePackageGift() throws Exception {
        // Initialize the database
        packageGiftRepository.saveAndFlush(packageGift);
        packageGiftSearchRepository.save(packageGift);
        int databaseSizeBeforeDelete = packageGiftRepository.findAll().size();

        // Get the packageGift
        restPackageGiftMockMvc.perform(delete("/api/package-gifts/{id}", packageGift.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean packageGiftExistsInEs = packageGiftSearchRepository.exists(packageGift.getId());
        assertThat(packageGiftExistsInEs).isFalse();

        // Validate the database is empty
        List<PackageGift> packageGifts = packageGiftRepository.findAll();
        assertThat(packageGifts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackageGift() throws Exception {
        // Initialize the database
        packageGiftRepository.saveAndFlush(packageGift);
        packageGiftSearchRepository.save(packageGift);

        // Search the packageGift
        restPackageGiftMockMvc.perform(get("/api/_search/package-gifts?query=id:" + packageGift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageGift.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)));
    }
}
