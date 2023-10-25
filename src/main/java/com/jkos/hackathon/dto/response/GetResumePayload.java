package com.jkos.hackathon.dto.response;

import com.jkos.hackathon.dto.ResumeDTO;

import java.util.List;

public record GetResumePayload(List<ResumeDTO> resumeList) {

}
