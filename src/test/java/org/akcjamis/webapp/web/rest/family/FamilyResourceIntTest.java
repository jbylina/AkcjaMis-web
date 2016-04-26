package org.akcjamis.webapp.web.rest.family;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.repository.FamilyRepository;
import org.akcjamis.webapp.service.FamilyService;
import org.akcjamis.webapp.repository.search.FamilySearchRepository;

import org.akcjamis.webapp.web.rest.FamilyResource;
import org.akcjamis.webapp.web.rest.TestUtil;
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
 * Test class for the FamilyResource REST controller.
 *
 * @see FamilyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class FamilyResourceIntTest {

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String UPDATED_STREET = "BBBBB";
    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NO = "BBBBBBBBBB";
    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String UPDATED_FLAT_NO = "BBBBBBBBBB";
    private static final String DEFAULT_POSTALCODE = "AAAAAA";
    private static final String UPDATED_POSTALCODE = "BBBBBB";
    private static final String DEFAULT_DISTRICT = "AAAAA";
    private static final String UPDATED_DISTRICT = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String UPDATED_REGION = "BBBBB";
    private static final String DEFAULT_SOURCE = "AAAAA";
    private static final String UPDATED_SOURCE = "BBBBB";

    @Inject
    private FamilyRepository familyRepository;

    @Inject
    private FamilyService familyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFamilyMockMvc;

    private Family family;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilyResource familyResource = new FamilyResource(familyService);
        this.restFamilyMockMvc = MockMvcBuilders.standaloneSetup(familyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        family = new Family();
        family.setStreet(DEFAULT_STREET);
        family.setHouseNo(DEFAULT_HOUSE_NO);
        family.setFlatNo(DEFAULT_FLAT_NO);
        family.setPostalcode(DEFAULT_POSTALCODE);
        family.setDistrict(DEFAULT_DISTRICT);
        family.setCity(DEFAULT_CITY);
        family.setRegion(DEFAULT_REGION);
        family.setSource(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createFamily() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // Create the Family
        restFamilyMockMvc.perform(post("/api/families")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isCreated());

        // Validate the Family in the database
        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeCreate + 1);
        Family testFamily = families.get(families.size() - 1);
        assertThat(testFamily.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testFamily.getHouseNo()).isEqualTo(DEFAULT_HOUSE_NO);
        assertThat(testFamily.getFlatNo()).isEqualTo(DEFAULT_FLAT_NO);
        assertThat(testFamily.getPostalcode()).isEqualTo(DEFAULT_POSTALCODE);
        assertThat(testFamily.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testFamily.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testFamily.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testFamily.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setStreet(null);

        // Create the Family, which fails.
        restFamilyMockMvc.perform(post("/api/families")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isBadRequest());

        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setHouseNo(null);

        // Create the Family, which fails.

        restFamilyMockMvc.perform(post("/api/families")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isBadRequest());

        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setCity(null);

        // Create the Family, which fails.

        restFamilyMockMvc.perform(post("/api/families")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isBadRequest());

        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilies() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the families
        restFamilyMockMvc.perform(get("/api/families?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
                .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO)))
                .andExpect(jsonPath("$.[*].flatNo").value(hasItem(DEFAULT_FLAT_NO)))
                .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE)))
                .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
                .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }

    @Test
    @Transactional
    public void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc.perform(get("/api/families/{id}", family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.houseNo").value(DEFAULT_HOUSE_NO))
            .andExpect(jsonPath("$.flatNo").value(DEFAULT_FLAT_NO))
            .andExpect(jsonPath("$.postalcode").value(DEFAULT_POSTALCODE))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE));
    }

    @Test
    @Transactional
    public void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get("/api/families/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamily() throws Exception {
        // Initialize the database
        familyService.save(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family
        Family updatedFamily = new Family();
        updatedFamily.setId(family.getId());
        updatedFamily.setStreet(UPDATED_STREET);
        updatedFamily.setHouseNo(UPDATED_HOUSE_NO);
        updatedFamily.setFlatNo(UPDATED_FLAT_NO);
        updatedFamily.setPostalcode(UPDATED_POSTALCODE);
        updatedFamily.setDistrict(UPDATED_DISTRICT);
        updatedFamily.setCity(UPDATED_CITY);
        updatedFamily.setRegion(UPDATED_REGION);
        updatedFamily.setSource(UPDATED_SOURCE);

        restFamilyMockMvc.perform(put("/api/families")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFamily)))
                .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = families.get(families.size() - 1);
        assertThat(testFamily.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testFamily.getHouseNo()).isEqualTo(UPDATED_HOUSE_NO);
        assertThat(testFamily.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testFamily.getPostalcode()).isEqualTo(UPDATED_POSTALCODE);
        assertThat(testFamily.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testFamily.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testFamily.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testFamily.getSource()).isEqualTo(UPDATED_SOURCE);

    }

    @Test
    @Transactional
    public void deleteFamily() throws Exception {
        // Initialize the database
        familyService.save(family);

        int databaseSizeBeforeDelete = familyRepository.findAll().size();

        // Get the family
        restFamilyMockMvc.perform(delete("/api/families/{id}", family.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        // Validate the database is empty
        List<Family> families = familyRepository.findAll();
        assertThat(families).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFamily() throws Exception {
        // Initialize the database
        familyService.save(family);

        // Search the family
        restFamilyMockMvc.perform(get("/api/_search/families?query=id:" + family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO)))
            .andExpect(jsonPath("$.[*].flatNo").value(hasItem(DEFAULT_FLAT_NO)))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }
}
