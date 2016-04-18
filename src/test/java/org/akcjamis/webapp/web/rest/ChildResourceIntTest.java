package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Child;
import org.akcjamis.webapp.repository.ChildRepository;
import org.akcjamis.webapp.repository.search.ChildSearchRepository;

import org.akcjamis.webapp.service.FamilyService;
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

import org.akcjamis.webapp.domain.enumeration.Sex;

/**
 * Test class for the ChildResource REST controller.
 *
 * @see ChildResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChildResourceIntTest {


    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Sex DEFAULT_SEX = Sex.MALE;
    private static final Sex UPDATED_SEX = Sex.FEMALE;

    private static final LocalDate DEFAULT_BIRTH_YEAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_YEAR = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ChildRepository childRepository;

    @Inject
    private ChildSearchRepository childSearchRepository;

    @Inject
    private FamilyService familyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChildMockMvc;

    private Child child;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChildResource childResource = new ChildResource(childRepository,
                                                        childSearchRepository,
                                                        familyService);
        ReflectionTestUtils.setField(childResource, "childSearchRepository", childSearchRepository);
        ReflectionTestUtils.setField(childResource, "childRepository", childRepository);
        this.restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        childSearchRepository.deleteAll();
        child = new Child();
        child.setNumber(DEFAULT_NUMBER);
        child.setFirstName(DEFAULT_FIRST_NAME);
        child.setLastName(DEFAULT_LAST_NAME);
        child.setSex(DEFAULT_SEX);
        child.setBirthYear(DEFAULT_BIRTH_YEAR);
    }

    @Test
    @Transactional
    public void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = children.get(children.size() - 1);
        assertThat(testChild.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testChild.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testChild.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testChild.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testChild.getBirthYear()).isEqualTo(DEFAULT_BIRTH_YEAR);

        // Validate the Child in ElasticSearch
        Child childEs = childSearchRepository.findOne(testChild.getId());
        assertThat(childEs).isEqualToComparingFieldByField(testChild);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setNumber(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isBadRequest());

        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setFirstName(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isBadRequest());

        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setLastName(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isBadRequest());

        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setSex(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isBadRequest());

        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setBirthYear(null);

        // Create the Child, which fails.

        restChildMockMvc.perform(post("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isBadRequest());

        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the children
        restChildMockMvc.perform(get("/api/children?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].birthYear").value(hasItem(DEFAULT_BIRTH_YEAR.toString())));
    }

    @Test
    @Transactional
    public void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.birthYear").value(DEFAULT_BIRTH_YEAR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);
        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = new Child();
        updatedChild.setId(child.getId());
        updatedChild.setNumber(UPDATED_NUMBER);
        updatedChild.setFirstName(UPDATED_FIRST_NAME);
        updatedChild.setLastName(UPDATED_LAST_NAME);
        updatedChild.setSex(UPDATED_SEX);
        updatedChild.setBirthYear(UPDATED_BIRTH_YEAR);

        restChildMockMvc.perform(put("/api/children")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChild)))
                .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeUpdate);
        Child testChild = children.get(children.size() - 1);
        assertThat(testChild.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testChild.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testChild.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testChild.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testChild.getBirthYear()).isEqualTo(UPDATED_BIRTH_YEAR);

        // Validate the Child in ElasticSearch
        Child childEs = childSearchRepository.findOne(testChild.getId());
        assertThat(childEs).isEqualToComparingFieldByField(testChild);
    }

    @Test
    @Transactional
    public void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);
        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Get the child
        restChildMockMvc.perform(delete("/api/children/{id}", child.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean childExistsInEs = childSearchRepository.exists(child.getId());
        assertThat(childExistsInEs).isFalse();

        // Validate the database is empty
        List<Child> children = childRepository.findAll();
        assertThat(children).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);

        // Search the child
        restChildMockMvc.perform(get("/api/_search/children?query=id:" + child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].birthYear").value(hasItem(DEFAULT_BIRTH_YEAR.toString())));
    }
}
