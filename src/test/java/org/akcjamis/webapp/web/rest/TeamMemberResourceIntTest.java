package org.akcjamis.webapp.web.rest;

import org.akcjamis.webapp.AkcjamisApp;
import org.akcjamis.webapp.domain.TeamMember;
import org.akcjamis.webapp.repository.TeamMemberRepository;
import org.akcjamis.webapp.repository.search.TeamMemberSearchRepository;

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
 * Test class for the TeamMemberResource REST controller.
 *
 * @see TeamMemberResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AkcjamisApp.class)
@WebAppConfiguration
@IntegrationTest
public class TeamMemberResourceIntTest {

    private static final String DEFAULT_USR_UUID = "AAAAA";
    private static final String UPDATED_USR_UUID = "BBBBB";

    @Inject
    private TeamMemberRepository teamMemberRepository;

    @Inject
    private TeamMemberSearchRepository teamMemberSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTeamMemberMockMvc;

    private TeamMember teamMember;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamMemberResource teamMemberResource = new TeamMemberResource();
        ReflectionTestUtils.setField(teamMemberResource, "teamMemberSearchRepository", teamMemberSearchRepository);
        ReflectionTestUtils.setField(teamMemberResource, "teamMemberRepository", teamMemberRepository);
        this.restTeamMemberMockMvc = MockMvcBuilders.standaloneSetup(teamMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        teamMemberSearchRepository.deleteAll();
        teamMember = new TeamMember();
        teamMember.setUsrUUID(DEFAULT_USR_UUID);
    }

    @Test
    @Transactional
    public void createTeamMember() throws Exception {
        int databaseSizeBeforeCreate = teamMemberRepository.findAll().size();

        // Create the TeamMember

        restTeamMemberMockMvc.perform(post("/api/team-members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teamMember)))
                .andExpect(status().isCreated());

        // Validate the TeamMember in the database
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        assertThat(teamMembers).hasSize(databaseSizeBeforeCreate + 1);
        TeamMember testTeamMember = teamMembers.get(teamMembers.size() - 1);
        assertThat(testTeamMember.getUsrUUID()).isEqualTo(DEFAULT_USR_UUID);

        // Validate the TeamMember in ElasticSearch
        TeamMember teamMemberEs = teamMemberSearchRepository.findOne(testTeamMember.getId());
        assertThat(teamMemberEs).isEqualToComparingFieldByField(testTeamMember);
    }

    @Test
    @Transactional
    public void getAllTeamMembers() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMembers
        restTeamMemberMockMvc.perform(get("/api/team-members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(teamMember.getId().intValue())))
                .andExpect(jsonPath("$.[*].usrUUID").value(hasItem(DEFAULT_USR_UUID.toString())));
    }

    @Test
    @Transactional
    public void getTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", teamMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(teamMember.getId().intValue()))
            .andExpect(jsonPath("$.usrUUID").value(DEFAULT_USR_UUID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeamMember() throws Exception {
        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);
        teamMemberSearchRepository.save(teamMember);
        int databaseSizeBeforeUpdate = teamMemberRepository.findAll().size();

        // Update the teamMember
        TeamMember updatedTeamMember = new TeamMember();
        updatedTeamMember.setId(teamMember.getId());
        updatedTeamMember.setUsrUUID(UPDATED_USR_UUID);

        restTeamMemberMockMvc.perform(put("/api/team-members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeamMember)))
                .andExpect(status().isOk());

        // Validate the TeamMember in the database
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        assertThat(teamMembers).hasSize(databaseSizeBeforeUpdate);
        TeamMember testTeamMember = teamMembers.get(teamMembers.size() - 1);
        assertThat(testTeamMember.getUsrUUID()).isEqualTo(UPDATED_USR_UUID);

        // Validate the TeamMember in ElasticSearch
        TeamMember teamMemberEs = teamMemberSearchRepository.findOne(testTeamMember.getId());
        assertThat(teamMemberEs).isEqualToComparingFieldByField(testTeamMember);
    }

    @Test
    @Transactional
    public void deleteTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);
        teamMemberSearchRepository.save(teamMember);
        int databaseSizeBeforeDelete = teamMemberRepository.findAll().size();

        // Get the teamMember
        restTeamMemberMockMvc.perform(delete("/api/team-members/{id}", teamMember.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean teamMemberExistsInEs = teamMemberSearchRepository.exists(teamMember.getId());
        assertThat(teamMemberExistsInEs).isFalse();

        // Validate the database is empty
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        assertThat(teamMembers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);
        teamMemberSearchRepository.save(teamMember);

        // Search the teamMember
        restTeamMemberMockMvc.perform(get("/api/_search/team-members?query=id:" + teamMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].usrUUID").value(hasItem(DEFAULT_USR_UUID.toString())));
    }
}
