package org.akcjamis.webapp.web.rest.family;

import com.google.common.collect.Sets;
import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.Family;
import org.akcjamis.webapp.domain.FamilyNote;
import org.akcjamis.webapp.domain.Tag;
import org.akcjamis.webapp.repository.FamilyNoteRepository;
import org.akcjamis.webapp.repository.FamilyRepository;
import org.akcjamis.webapp.repository.TagRepository;
import org.akcjamis.webapp.service.FamilyNoteService;

import org.akcjamis.webapp.web.rest.FamilyNoteResource;
import org.akcjamis.webapp.web.rest.TestUtil;
import org.akcjamis.webapp.web.rest.dto.FamilyNoteDTO;
import org.akcjamis.webapp.web.rest.mapper.FamilyMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FamilyNoteResource REST controller.
 *
 * @see FamilyNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class FamilyNoteResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBBBBBBB";

    private static final String DEFAULT_CODE_1 = "AAA";
    private static final String DEFAULT_COLOR_1 = "AAAAAAAAAAAA";

    private static final String DEFAULT_CODE_2 = "BBB";
    private static final String DEFAULT_COLOR_2 = "BBBBBBBBBBBB";

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String DEFAULT_POSTALCODE = "AAAAAA";
    private static final String DEFAULT_DISTRICT = "AAAAA";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String DEFAULT_SOURCE = "AAAAA";

    @Inject
    private FamilyNoteRepository familyNoteRepository;

    @Inject
    private FamilyNoteService familyNoteService;

    @Inject
    FamilyMapper mapper;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private FamilyRepository familyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFamilyNoteMockMvc;

    private FamilyNoteDTO familyNoteDTO;

    private FamilyNote familyNote;

    private Family family;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilyNoteResource familyNoteResource = new FamilyNoteResource(familyNoteService, mapper);
        this.restFamilyNoteMockMvc = MockMvcBuilders.standaloneSetup(familyNoteResource)
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
        family = familyRepository.save(family);

        Tag tag1 = new Tag();
        tag1.setCode(DEFAULT_CODE_1);
        tag1.setColor(DEFAULT_COLOR_1);
        tagRepository.save(tag1);
        Tag tag2 = new Tag();
        tag2.setCode(DEFAULT_CODE_2);
        tag2.setColor(DEFAULT_COLOR_2);
        tagRepository.save(tag2);

        familyNoteDTO = new FamilyNoteDTO();
        familyNoteDTO.setContent(DEFAULT_CONTENT);
        familyNoteDTO.setArchived(DEFAULT_ARCHIVED);
        familyNoteDTO.setTags(Sets.newHashSet(DEFAULT_CODE_1, DEFAULT_CODE_2));

        familyNote = new FamilyNote();
        familyNote.setContent(DEFAULT_CONTENT);
        familyNote.setArchived(DEFAULT_ARCHIVED);
        familyNote.setTags(Sets.newHashSet(tag1, tag2));
    }

    @Test
    @Transactional
    public void createFamilyNote() throws Exception {
        int databaseSizeBeforeCreate = familyNoteRepository.findAll().size();

        // Create the FamilyNote
        restFamilyNoteMockMvc.perform(post("/api/families/{id}/family-notes", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNoteDTO)))
                .andExpect(status().isCreated());

        // Validate the FamilyNote in the database
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeCreate + 1);
        FamilyNote testFamilyNote = familyNotes.get(familyNotes.size() - 1);
        assertThat(testFamilyNote.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFamilyNote.isArchived()).isEqualTo(DEFAULT_ARCHIVED);
        assertThat(testFamilyNote.getTags().stream().map(Tag::getCode).collect(Collectors.toSet()))
            .isEqualTo(Sets.newHashSet(DEFAULT_CODE_1, DEFAULT_CODE_2));
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyNoteRepository.findAll().size();
        // set the field null
        familyNoteDTO.setContent(null);

        // Create the FamilyNote, which fails.
        restFamilyNoteMockMvc.perform(post("/api/families/{id}/family-notes", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNoteDTO)))
                .andExpect(status().isBadRequest());

        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArchivedIsFilledWithFalse() throws Exception {
        int databaseSizeBeforeTest = familyNoteRepository.findAll().size();
        // set the field null
        familyNoteDTO.setArchived(null);

        // Create the FamilyNote, which fails.
        restFamilyNoteMockMvc.perform(post("/api/families/{id}/family-notes", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(familyNoteDTO)))
                .andExpect(status().isCreated());

        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeTest + 1);
        FamilyNote familyNote = familyNotes.get(familyNotes.size() - 1);
        assertThat(familyNote.isArchived()).isEqualTo(false);
    }

    @Test
    @Transactional
    public void getAllFamilyNotes() throws Exception {
        familyNote.setFamily(family);
        // Initialize the database
        familyNoteRepository.saveAndFlush(familyNote);
        //TODO zmiana na service


        // Get all the familyNotes
        restFamilyNoteMockMvc.perform(get("/api/families/{id}/family-notes?sort=id,desc", family.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(familyNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED)));
    }

    @Test
    @Transactional
    public void getFamilyNote() throws Exception {
        familyNote.setFamily(family);
        // Initialize the database
        familyNoteRepository.saveAndFlush(familyNote);

        // Get the familyNoteDTO
        restFamilyNoteMockMvc.perform(get("/api/families/{id}/family-notes/{id}", family.getId(), familyNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(familyNote.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED));
    }

    @Test
    @Transactional
    public void getNonExistingFamilyNote() throws Exception {
        // Get the familyNoteDTO
        restFamilyNoteMockMvc.perform(get("/api/families/{id}/family-notes/{id}", family.getId(), Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilyNote() throws Exception {
        // Initialize the database
        familyNote = familyNoteService.save(family.getId(), familyNote);
        int databaseSizeBeforeUpdate = familyNoteRepository.findAll().size();

        // Update the familyNoteDTO
        FamilyNoteDTO updatedFamilyNoteDTO = new FamilyNoteDTO();
        updatedFamilyNoteDTO.setId(familyNote.getId());
        updatedFamilyNoteDTO.setContent(UPDATED_CONTENT);

        restFamilyNoteMockMvc.perform(put("/api/families/{id}/family-notes", family.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFamilyNoteDTO)))
                .andExpect(status().isOk());

        // Validate the FamilyNote in the database
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeUpdate);
        FamilyNote testFamilyNote = familyNotes.get(familyNotes.size() - 1);
        assertThat(testFamilyNote.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteFamilyNote() throws Exception {
        // Initialize the database
        familyNote = familyNoteService.save(family.getId(), familyNote);
        int databaseSizeBeforeDelete = familyNoteRepository.findAll().size();

        // Get the familyNoteDTO
        restFamilyNoteMockMvc.perform(delete("/api/families/{id}/family-notes/{id}", family.getId(), familyNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FamilyNote> familyNotes = familyNoteRepository.findAll();
        assertThat(familyNotes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
