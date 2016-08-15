package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.*;
import org.akcjamis.webapp.repository.*;

import org.akcjamis.webapp.service.ChristmasPackageService;
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

    private static final Integer DEFAULT_MARK = 1;

    private static final Boolean DEFAULT_DELIVERED = false;

    private static final Short DEFAULT_YEAR = 2016;

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String DEFAULT_POSTALCODE = "AAAAAA";
    private static final String DEFAULT_DISTRICT = "AAAAA";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String DEFAULT_SOURCE = "AAAAA";

    @Inject
    private ChristmasPackageChangeRepository christmasPackageChangeRepository;

    @Inject
    private ChristmasPackageRepository christmasPackageRepository;

    @Inject
    private ChristmasPackageService christmasPackageService;

    @Inject
    private FamilyRepository familyRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChristmasPackageChangeMockMvc;

    private ChristmasPackageChange christmasPackageChange;

    private ChristmasPackage christmasPackage;

    private Family family;

    private Event event;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageChangeResource christmasPackageChangeResource = new ChristmasPackageChangeResource(christmasPackageService);
        this.restChristmasPackageChangeMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageChangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        christmasPackageChangeRepository.deleteAll();
        christmasPackageChange = new ChristmasPackageChange();
        christmasPackageChange.setTypeCode(DEFAULT_TYPE_CODE);
        christmasPackageChange.setTime(DEFAULT_TIME);
        christmasPackageChange.setContent(DEFAULT_CONTENT);

        eventRepository.deleteAll();
        event = new Event();
        event.setYear(DEFAULT_YEAR);
        event = eventRepository.save(event);

        familyRepository.deleteAll();
        family = new Family();
        family.setStreet(DEFAULT_STREET);
        family.setHouseNo(DEFAULT_HOUSE_NO);
        family.setFlatNo(DEFAULT_FLAT_NO);
        family.setPostalcode(DEFAULT_POSTALCODE);
        family.setDistrict(DEFAULT_DISTRICT);
        family.setCity(DEFAULT_CITY);
        family.setRegion(DEFAULT_REGION);
        family.setSource(DEFAULT_SOURCE);
        family = familyRepository.save(family);

        christmasPackageRepository.deleteAll();
        christmasPackage = new ChristmasPackage();
        christmasPackage.setMark(DEFAULT_MARK);
        christmasPackage.setDelivered(DEFAULT_DELIVERED);
        christmasPackage.setEvent(event);
        christmasPackage.setFamily(family);
        christmasPackage = christmasPackageRepository.save(christmasPackage);
    }

    @Test
    @Transactional
    public void createChristmasPackageChange() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageChangeRepository.findAll().size();

        // Create the ChristmasPackageChange

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package/{id}/changes", christmasPackage.getId())
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

    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = christmasPackageChangeRepository.findAll().size();
        // set the field null
        christmasPackageChange.setTime(null);

        // Create the ChristmasPackageChange, which fails.

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package/{id}/changes", christmasPackage.getId())
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

        restChristmasPackageChangeMockMvc.perform(post("/api/christmas-package/{id}/changes", christmasPackage.getId())
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
        christmasPackageChange.setChristmasPackage(christmasPackage);
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);

        // Get all the christmasPackageChanges
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package/{id}/changes?sort=id,desc", christmasPackage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackageChange.getId().intValue())))
                .andExpect(jsonPath("$.[*].type_code").value(hasItem(DEFAULT_TYPE_CODE)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChange.setChristmasPackage(christmasPackage);
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);

        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package/{packageId}/changes/{id}", christmasPackage.getId(), christmasPackageChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackageChange.getId().intValue()))
            .andExpect(jsonPath("$.type_code").value(DEFAULT_TYPE_CODE))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackageChange() throws Exception {
        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(get("/api/christmas-package/{packageId}/changes/{id}", christmasPackage.getId(), Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChange.setChristmasPackage(christmasPackage);
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);
        int databaseSizeBeforeUpdate = christmasPackageChangeRepository.findAll().size();

        // Update the christmasPackageChange
        ChristmasPackageChange updatedChristmasPackageChange = new ChristmasPackageChange();
        updatedChristmasPackageChange.setId(christmasPackageChange.getId());
        updatedChristmasPackageChange.setTypeCode(UPDATED_TYPE_CODE);
        updatedChristmasPackageChange.setTime(UPDATED_TIME);
        updatedChristmasPackageChange.setContent(UPDATED_CONTENT);

        restChristmasPackageChangeMockMvc.perform(put("/api/christmas-package/{id}/changes", christmasPackage.getId())
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

    }

    @Test
    @Transactional
    public void deleteChristmasPackageChange() throws Exception {
        // Initialize the database
        christmasPackageChange.setChristmasPackage(christmasPackage);
        christmasPackageChangeRepository.saveAndFlush(christmasPackageChange);
        int databaseSizeBeforeDelete = christmasPackageChangeRepository.findAll().size();

        // Get the christmasPackageChange
        restChristmasPackageChangeMockMvc.perform(delete("/api/christmas-package/{packageId}/changes/{id}", christmasPackage.getId(), christmasPackageChange.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChristmasPackageChange> christmasPackageChanges = christmasPackageChangeRepository.findAll();
        assertThat(christmasPackageChanges).hasSize(databaseSizeBeforeDelete - 1);
    }
}
