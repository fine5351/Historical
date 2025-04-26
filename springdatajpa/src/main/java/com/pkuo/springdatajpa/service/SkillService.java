package com.pkuo.springdatajpa.service;

import com.pkuo.springdatajpa.constant.SkillLevel;
import com.pkuo.springdatajpa.dto.AllowedSkillLevelMappingDTO;
import com.pkuo.springdatajpa.entity.AllowedSkillMapping;
import com.pkuo.springdatajpa.entity.Skill;
import com.pkuo.springdatajpa.repository.AllowedSkillMappingRepository;
import com.pkuo.springdatajpa.repository.SkillRepository;
import com.pkuo.springdatajpa.repository.TeamRepository;
import com.pkuo.springdatajpa.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final AllowedSkillMappingRepository allowedSkillMappingRepository;
    private final TeamRepository teamRepository;
    private final VacancyRepository vacancyRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<AllowedSkillMapping> getAllowedSkillMapping() {
        return allowedSkillMappingRepository.findAll();
    }

    public String getSkillNameById(long id) {
        return skillRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id %s not found in skill".formatted(id))).getName();
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
        // teamId,vacancy_id, skillId, levelId
        String[] values = line.split(",");
        long teamId = Long.parseLong(values[0]);
        long vacancyId = Long.parseLong(values[1]);
        long skillId = Long.parseLong(values[2]);
        int levelId = Integer.parseInt(values[3]);
        AllowedSkillMapping allowedSkillMapping = new AllowedSkillMapping();
        allowedSkillMapping.setTeamId(teamId);
        allowedSkillMapping.setVacancyId(vacancyId);
        allowedSkillMapping.setSkillId(skillId);
        allowedSkillMapping.setSkillLevel(SkillLevel.getFromOrdinal(levelId));
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
        return teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id %s not found in team".formatted(id))).getName();
    }

    public String getVacancyTitleById(long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id %s not found in vacancy".formatted(id))).getTitle();
    }

}
