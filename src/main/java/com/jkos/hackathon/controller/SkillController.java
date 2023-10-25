package com.jkos.hackathon.controller;

import com.jkos.hackathon.constant.ResponseStatusCode;
import com.jkos.hackathon.dto.AllowedSkillLevelMappingDTO;
import com.jkos.hackathon.dto.SkillDTO;
import com.jkos.hackathon.dto.response.BaseResponse;
import com.jkos.hackathon.dto.response.GetAllowedSkillLevelPayload;
import com.jkos.hackathon.dto.response.GetSkillPayload;
import com.jkos.hackathon.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
