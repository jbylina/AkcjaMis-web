package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.ChristmasPackage;
import org.akcjamis.webapp.domain.Event;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.repository.ChristmasPackageRepository;
import org.akcjamis.webapp.repository.EventRepository;
import org.akcjamis.webapp.repository.FamilyRepository;
import org.akcjamis.webapp.service.ChristmasPackageService;
import org.akcjamis.webapp.web.rest.dto.ChristmasPackageDTO;
import org.akcjamis.webapp.web.rest.mapper.ChristmasPackageMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
    private ChristmasPackageRepository christmasPackageRepository;

    @Inject
    private ChristmasPackageService christmasPackageService;

    @Inject
    private ChristmasPackageMapper mapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private FamilyRepository familyRepository;

    private MockMvc restChristmasPackageMockMvc;

    private ChristmasPackage christmasPackage;

    private ChristmasPackageDTO christmasPackageDTO;

    private Event event;

    private Family family;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChristmasPackageResource christmasPackageResource = new ChristmasPackageResource(christmasPackageService, mapper);
        this.restChristmasPackageMockMvc = MockMvcBuilders.standaloneSetup(christmasPackageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        event = new Event();
        event.setYear(DEFAULT_YEAR);
        eventRepository.save(event);

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

        christmasPackage = new ChristmasPackage();
        christmasPackage.setMark(DEFAULT_MARK);
        christmasPackage.setDelivered(DEFAULT_DELIVERED);

        christmasPackageDTO = new ChristmasPackageDTO();
        christmasPackageDTO.setMark(DEFAULT_MARK);
        christmasPackageDTO.setFamilyId(family.getId());
    }

    @Test
    @Transactional
    public void createChristmasPackage() throws Exception {
        int databaseSizeBeforeCreate = christmasPackageRepository.findAll().size();

        // Create the ChristmasPackage
        restChristmasPackageMockMvc.perform(post("/api/events/{year}/christmas-packages", DEFAULT_YEAR)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageDTO)))
                .andExpect(status().isCreated());

        // Validate the ChristmasPackage in the database
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeCreate + 1);
        ChristmasPackage testChristmasPackage = christmasPackages.get(christmasPackages.size() - 1);
        assertThat(testChristmasPackage.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testChristmasPackage.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testChristmasPackage.getEvent().getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkDeliveredIsSetToFalse() throws Exception {
        int databaseSizeBeforeTest = christmasPackageRepository.findAll().size();
        // set the field null
        christmasPackageDTO.setDelivered(true);

        // Create the ChristmasPackage, which fails.
        restChristmasPackageMockMvc.perform(post("/api/events/{year}/christmas-packages", DEFAULT_YEAR)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(christmasPackageDTO)))
                .andExpect(status().isCreated());

        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeTest + 1);
        ChristmasPackage testChristmasPackage = christmasPackages.get(christmasPackages.size() - 1);
        assertThat(testChristmasPackage.isDelivered()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @Transactional
    public void getAllChristmasPackages() throws Exception {
        christmasPackage.setFamily(family);
        christmasPackage.setEvent(event);
        christmasPackageRepository.saveAndFlush(christmasPackage);

        // Get all the christmasPackages
        restChristmasPackageMockMvc.perform(get("/api/events/{year}/christmas-packages?sort=id,desc", DEFAULT_YEAR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackage.getId().intValue())))
                .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
                .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)));
    }

    @Test
    @Transactional
    public void getChristmasPackage() throws Exception {
        christmasPackage.setFamily(family);
        christmasPackage.setEvent(event);
        christmasPackageRepository.saveAndFlush(christmasPackage);

        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/events/{year}/christmas-packages/{id}", DEFAULT_YEAR, christmasPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(christmasPackage.getId().intValue()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED));
    }

    @Test
    @Transactional
    public void getNonExistingChristmasPackage() throws Exception {
        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/events/{year}/christmas-packages/{id}", DEFAULT_YEAR, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChristmasPackage() throws Exception {
        christmasPackage.setFamily(family);
        christmasPackage.setEvent(event);
        christmasPackageService.savePackage(DEFAULT_YEAR, christmasPackage);

        int databaseSizeBeforeUpdate = christmasPackageRepository.findAll().size();

        // Update the christmasPackage
        ChristmasPackageDTO updatedChristmasPackageDTO = new ChristmasPackageDTO();
        updatedChristmasPackageDTO.setId(christmasPackage.getId());
        updatedChristmasPackageDTO.setMark(UPDATED_MARK);
        updatedChristmasPackageDTO.setDelivered(UPDATED_DELIVERED);
        updatedChristmasPackageDTO.setFamilyId(family.getId());

        restChristmasPackageMockMvc.perform(put("/api/events/{year}/christmas-packages", DEFAULT_YEAR)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChristmasPackageDTO)))
                .andExpect(status().isOk());

        // Validate the ChristmasPackage in the database
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeUpdate);
        ChristmasPackage testChristmasPackage = christmasPackages.get(christmasPackages.size() - 1);
        assertThat(testChristmasPackage.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testChristmasPackage.isDelivered()).isEqualTo(Boolean.FALSE);

    }

    @Test
    @Transactional
    public void deleteChristmasPackage() throws Exception {
        christmasPackage.setFamily(family);
        christmasPackage.setEvent(event);
        christmasPackageService.savePackage(DEFAULT_YEAR, christmasPackage);

        int databaseSizeBeforeDelete = christmasPackageRepository.findAll().size();

        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(delete("/api/events/{year}/christmas-packages/{id}", DEFAULT_YEAR, christmasPackage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChristmasPackage> christmasPackages = christmasPackageRepository.findAll();
        assertThat(christmasPackages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void getChristmasPackageList() throws Exception {
        christmasPackage.setFamily(family);
        christmasPackage.setEvent(event);
        christmasPackageRepository.saveAndFlush(christmasPackage);

        // Get the christmasPackage
        restChristmasPackageMockMvc.perform(get("/api/events/{year}/christmas-packages-list", DEFAULT_YEAR))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(christmasPackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)));
    }
}
