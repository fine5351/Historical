package com.pkuo.spring_data_jpa.controller;

import com.pkuo.spring_data_jpa.constant.ResponseStatusCode;
import com.pkuo.spring_data_jpa.dto.AllowedSkillLevelMappingDTO;
import com.pkuo.spring_data_jpa.dto.SkillDTO;
import com.pkuo.spring_data_jpa.dto.response.BaseResponse;
import com.pkuo.spring_data_jpa.dto.response.GetAllowedSkillLevelPayload;
import com.pkuo.spring_data_jpa.dto.response.GetSkillPayload;
import com.pkuo.spring_data_jpa.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Comparator.comparing;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/skill")
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/")
    public BaseResponse<GetSkillPayload> getSkill() {
        List<SkillDTO> list = skillService.getAllSkills().stream()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
                .sorted(comparing(SkillDTO::getName))
                .toList();
        return BaseResponse.success(new GetSkillPayload(list));
    }

    @GetMapping("/allowedSkillLevel")
    public BaseResponse<GetAllowedSkillLevelPayload> getAllowedSkillLevel() {
        List<AllowedSkillLevelMappingDTO> list = skillService.getAllowedSkillMapping()
                .stream()
                .map(skillService::convert)
                .toList();
        return BaseResponse.success(new GetAllowedSkillLevelPayload(list));
    }

    @PostMapping("/allowedSkillLevel")
    public BaseResponse<Void> registerAllowedSkillLevel(@RequestParam("file") MultipartFile file) {
        try {
            skillService.truncateAllowedSkillMapping();
            skillService.registerAllowedSkillMapping(file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResponse.fail(ResponseStatusCode.FAILURE.getCode(), e.getMessage());
        }
        return BaseResponse.success();
    }

}
