package com.jkos.hackathon.controller;

import com.jkos.hackathon.constant.ResumeStatus;
import com.jkos.hackathon.dto.ResumeDTO;
import com.jkos.hackathon.dto.response.BaseResponse;
import com.jkos.hackathon.dto.response.GetResumePayload;
import com.jkos.hackathon.entity.Resume;
import com.jkos.hackathon.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeRepository resumeRepository;

    @GetMapping("/")
    public BaseResponse<GetResumePayload> getResume() {
        List<ResumeDTO> list = resumeRepository.findAll().stream()
                .map(resume -> new ResumeDTO(resume.getFileName(), resume.getStatus().ordinal()))
                .toList();

        return BaseResponse.success(new GetResumePayload(list));
    }

    @PostMapping("/")
    public BaseResponse<Void> createResume(@RequestPart("file") MultipartFile file) {
        log.info("resume");
        Resume entity = buildResume(file);
        resumeRepository.save(entity);
        return BaseResponse.success();
    }

    private Resume buildResume(MultipartFile file) {
        Resume entity = new Resume();
        entity.setFileName(file.getName());
        entity.setStatus(ResumeStatus.PENDING);
        return entity;
    }

}
