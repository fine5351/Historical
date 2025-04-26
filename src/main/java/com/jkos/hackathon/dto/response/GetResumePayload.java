package com.pkuo.springdatajpa.dto.response;

import com.pkuo.springdatajpa.dto.ResumeDTO;

import java.util.List;

public record GetResumePayload(List<ResumeDTO> resumeList) {

}
