package com.finekuo.springdatajpa.service;

import com.finekuo.normalcore.constant.SkillLevel;
import com.finekuo.springdatajpa.dto.AllowedSkillLevelMappingDTO;
import com.finekuo.springdatajpa.entity.AllowedSkillMapping;
import com.finekuo.springdatajpa.entity.Skill;
import com.finekuo.springdatajpa.repository.AllowedSkillMappingRepository;
import com.finekuo.springdatajpa.repository.SkillRepository;
import com.finekuo.springdatajpa.repository.TeamRepository;
import com.finekuo.springdatajpa.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Slf4j
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final AllowedSkillMappingRepository allowedSkillMappingRepository;
    private final TeamRepository teamRepository;
    private final VacancyRepository vacancyRepository;

    public List<Skill> getAllSkills() {
        return StreamSupport.stream(skillRepository.findAll().spliterator(), false).toList();
    }

    public List<AllowedSkillMapping> getAllowedSkillMapping() {
        return StreamSupport.stream(allowedSkillMappingRepository.findAll().spliterator(), false).toList();
    }

    public String getSkillNameById(long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id %s not found in skill".formatted(id))).getName();
    }

    @Transactional
    public void registerAllowedSkillMapping(MultipartFile file) throws IOException {
        List<AllowedSkillMapping> allowedSkillMappings = new ArrayList<>();
        String content = new String(file.getBytes());
        String[] lines = content.split("\n");
        int lineCount = 0;
        for (String line : lines) {
            lineCount++;
            if (lineCount == 1) {
                continue;
            }
            allowedSkillMappings.add(buildEntity(line));
        }
        allowedSkillMappingRepository.saveAll(allowedSkillMappings);
    }

    private AllowedSkillMapping buildEntity(String line) {
        // CSV format from test is "Skill Name,Skill Level"
        String[] values = line.split(",");
        String skillName = values[0].trim();
        String skillLevelStr = values[1].trim();

        Skill skill = skillRepository.findByName(skillName)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found: " + skillName + ". Ensure skills are pre-loaded if CSV relies on existing names."));
        
        AllowedSkillMapping allowedSkillMapping = new AllowedSkillMapping();
        allowedSkillMapping.setSkillId(skill.getId());
        allowedSkillMapping.setSkillLevel(SkillLevel.valueOf(skillLevelStr.toUpperCase())); // Convert string to enum

        // teamId and vacancyId are not in this CSV. Setting to a placeholder.
        // These are mandatory (long primitive) in AllowedSkillMapping.
        // This implies either the CSV should provide them, they should have defaults,
        // or the entity should allow them to be nullable if appropriate for global skill mappings.
        allowedSkillMapping.setTeamId(0L); // Placeholder - assuming 0 is a valid or ignorable ID
        allowedSkillMapping.setVacancyId(0L); // Placeholder - assuming 0 is a valid or ignorable ID
        
        return allowedSkillMapping;
    }

    @Transactional
    public void truncateAllowedSkillMapping() {
        allowedSkillMappingRepository.truncate();
    }

    public AllowedSkillLevelMappingDTO convert(AllowedSkillMapping entity) {
        AllowedSkillLevelMappingDTO dto = new AllowedSkillLevelMappingDTO();
        dto.setTeamName(getTeamNameById(entity.getTeamId()));
        dto.setSkillName(getSkillNameById(entity.getSkillId()));
        dto.setVacancyTitle(getVacancyTitleById(entity.getVacancyId()));
        dto.setSkillLevel(entity.getSkillLevel().name().toLowerCase());
        return dto;
    }

    public String getTeamNameById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id %s not found in team".formatted(id))).getName();
    }

    public String getVacancyTitleById(long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id %s not found in vacancy".formatted(id))).getTitle();
    }

}
