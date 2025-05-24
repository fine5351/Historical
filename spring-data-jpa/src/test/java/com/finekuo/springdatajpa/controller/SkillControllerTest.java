package com.finekuo.springdatajpa.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.normalcore.constant.ResponseStatusCode;
import com.finekuo.normalcore.constant.SkillLevel;
import com.finekuo.normalcore.dto.response.BaseResponse;
import com.finekuo.springdatajpa.dto.AllowedSkillLevelMappingDTO;
import com.finekuo.springdatajpa.dto.SkillDTO;
import com.finekuo.springdatajpa.dto.response.GetAllowedSkillLevelPayload;
import com.finekuo.springdatajpa.dto.response.GetSkillPayload;
import com.finekuo.springdatajpa.entity.AllowedSkillMapping;
import com.finekuo.springdatajpa.entity.Skill;
import com.finekuo.springdatajpa.entity.Team;
import com.finekuo.springdatajpa.entity.Vacancy;
import com.finekuo.springdatajpa.repository.AllowedSkillMappingRepository;
import com.finekuo.springdatajpa.repository.SkillRepository;
import com.finekuo.springdatajpa.repository.TeamRepository;
import com.finekuo.springdatajpa.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AllowedSkillMappingRepository allowedSkillMappingRepository;

    @Autowired
    private TeamRepository teamRepository; // Added for dummy data

    @Autowired
    private VacancyRepository vacancyRepository; // Added for dummy data

    @BeforeEach
    public void setUp() {
        allowedSkillMappingRepository.deleteAll(); 
        skillRepository.deleteAll();
        teamRepository.deleteAll();
        vacancyRepository.deleteAll();
    }

    @Test
    public void getSkill_shouldReturnSkillsSortedByName() throws Exception {
        Skill skill1 = new Skill();
        skill1.setName("Java");

        Skill skill2 = new Skill();
        skill2.setName("Python");
        
        Skill skill3 = new Skill();
        skill3.setName("C++");

        List<Skill> savedSkills = StreamSupport.stream(
            skillRepository.saveAll(Arrays.asList(skill1, skill2, skill3)).spliterator(), false)
            .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/skill/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<GetSkillPayload> response = objectMapper.readValue(contentAsString, new TypeReference<BaseResponse<GetSkillPayload>>() {});

        assertThat(response.getCode()).isEqualTo(ResponseStatusCode.SUCCESS.getCode()); // Corrected assertion
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNotNull();
        
        List<SkillDTO> actualDtos = response.getData().skills(); 
        assertThat(actualDtos).hasSize(3);

        List<SkillDTO> expectedDtos = savedSkills.stream()
                .map(skill -> {
                    SkillDTO dto = new SkillDTO();
                    dto.setId(skill.getId());
                    dto.setName(skill.getName());
                    return dto;
                })
                .sorted(Comparator.comparing(SkillDTO::getName))
                .collect(Collectors.toList());
        
        for (int i = 0; i < expectedDtos.size(); i++) {
            SkillDTO expected = expectedDtos.get(i);
            SkillDTO actual = actualDtos.get(i);
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
        }
    }

    @Test
    public void getAllowedSkillLevel_shouldReturnAllowedSkillLevels() throws Exception {
        Team team = new Team();
        team.setName("Test Team");
        teamRepository.save(team);

        Vacancy vacancy = new Vacancy();
        vacancy.setTitle("Test Vacancy");
        vacancyRepository.save(vacancy);

        Skill javaSkill = new Skill();
        javaSkill.setName("Java");
        skillRepository.save(javaSkill);

        Skill pythonSkill = new Skill();
        pythonSkill.setName("Python");
        skillRepository.save(pythonSkill);
        
        AllowedSkillMapping mapping1 = new AllowedSkillMapping();
        mapping1.setTeamId(team.getId()); // Set valid teamId
        mapping1.setVacancyId(vacancy.getId()); // Set valid vacancyId
        mapping1.setSkillId(javaSkill.getId()); 
        mapping1.setSkillLevel(SkillLevel.ADVANCED); 

        AllowedSkillMapping mapping2 = new AllowedSkillMapping();
        mapping2.setTeamId(team.getId()); // Set valid teamId
        mapping2.setVacancyId(vacancy.getId()); // Set valid vacancyId
        mapping2.setSkillId(pythonSkill.getId()); 
        mapping2.setSkillLevel(SkillLevel.INTERMEDIATE); 

        allowedSkillMappingRepository.saveAll(Arrays.asList(mapping1, mapping2));

        MvcResult result = mockMvc.perform(get("/skill/allowedSkillLevel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<GetAllowedSkillLevelPayload> response = objectMapper.readValue(contentAsString, new TypeReference<BaseResponse<GetAllowedSkillLevelPayload>>() {});

        assertThat(response.getCode()).isEqualTo(ResponseStatusCode.SUCCESS.getCode()); // Corrected assertion
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNotNull();
        
        List<AllowedSkillLevelMappingDTO> actualDtos = response.getData().allowedSkillLevelMappings(); 
        assertThat(actualDtos).hasSize(2);

        actualDtos.sort(Comparator.comparing(AllowedSkillLevelMappingDTO::getSkillName));

        assertThat(actualDtos.get(0).getSkillName()).isEqualTo("Java");
        assertThat(actualDtos.get(0).getSkillLevel()).isEqualTo(SkillLevel.ADVANCED.name()); 
        assertThat(actualDtos.get(1).getSkillName()).isEqualTo("Python");
        assertThat(actualDtos.get(1).getSkillLevel()).isEqualTo(SkillLevel.INTERMEDIATE.name()); 
    }

    @Test
    public void registerAllowedSkillLevel_shouldSucceedAndPersistData() throws Exception {
        Skill java = new Skill(); 
        java.setName("Java"); 
        skillRepository.save(java);
        
        Skill python = new Skill();
        python.setName("Python");
        skillRepository.save(python);

        Skill springBoot = new Skill();
        springBoot.setName("Spring Boot");
        skillRepository.save(springBoot);

        String csvContent = "Skill Name,Skill Level\nJava,ADVANCED\nPython,INTERMEDIATE\nSpring Boot,ADVANCED";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "skills.csv",
                MediaType.TEXT_PLAIN_VALUE,
                csvContent.getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/skill/allowedSkillLevel")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<Void> response = objectMapper.readValue(contentAsString, new TypeReference<BaseResponse<Void>>() {});

        assertThat(response.getCode()).isEqualTo(ResponseStatusCode.SUCCESS.getCode()); // Corrected assertion
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNull();

        List<AllowedSkillMapping> persistedMappings = StreamSupport.stream(
                allowedSkillMappingRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertThat(persistedMappings).hasSize(3); 

        persistedMappings.sort(Comparator.comparing(m -> skillRepository.findById(m.getSkillId()).get().getName()));

        AllowedSkillMapping javaMapping = persistedMappings.get(0);
        assertThat(skillRepository.findById(javaMapping.getSkillId()).get().getName()).isEqualTo("Java");
        assertThat(javaMapping.getSkillLevel()).isEqualTo(SkillLevel.ADVANCED); 

        AllowedSkillMapping pythonMapping = persistedMappings.get(1);
        assertThat(skillRepository.findById(pythonMapping.getSkillId()).get().getName()).isEqualTo("Python");
        assertThat(pythonMapping.getSkillLevel()).isEqualTo(SkillLevel.INTERMEDIATE); 
        
        AllowedSkillMapping springBootMapping = persistedMappings.get(2);
        assertThat(skillRepository.findById(springBootMapping.getSkillId()).get().getName()).isEqualTo("Spring Boot");
        assertThat(springBootMapping.getSkillLevel()).isEqualTo(SkillLevel.ADVANCED); 
    }
}
