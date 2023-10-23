package com.jkos.hackathon.controller;

import com.jkos.hackathon.dto.BaseResponse;
import com.jkos.hackathon.entity.Skill;
import com.jkos.hackathon.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    @GetMapping("/skill")
    public BaseResponse<List<Skill>> getSkill() {
        return BaseResponse.success(skillRepository.findAll());
    }

}
