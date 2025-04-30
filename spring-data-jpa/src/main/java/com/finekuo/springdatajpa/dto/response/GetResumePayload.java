package com.finekuo.springdatajpa.dto.response;

import com.finekuo.springdatajpa.dto.ResumeDTO;

import java.util.List;

public record GetResumePayload(List<ResumeDTO> resumeList) {

}
