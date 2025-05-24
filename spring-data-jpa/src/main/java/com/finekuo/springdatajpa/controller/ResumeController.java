package com.finekuo.springdatajpa.controller;

import com.finekuo.normalcore.constant.ResumeStatus;
import com.finekuo.normalcore.dto.response.BaseResponse;
import com.finekuo.springdatajpa.dto.ResumeDTO;
import com.finekuo.springdatajpa.dto.response.GetResumePayload;
import com.finekuo.springdatajpa.entity.Resume;
import com.finekuo.springdatajpa.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeRepository resumeRepository;

    @GetMapping("/")
    public BaseResponse<GetResumePayload> getResume() {
        List<ResumeDTO> list = StreamSupport.stream(resumeRepository.findAll().spliterator(), false)
                .map(ResumeDTO::fromEntity) // Use the static factory method
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
