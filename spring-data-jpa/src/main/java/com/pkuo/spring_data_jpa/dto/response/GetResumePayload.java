package com.pkuo.spring_data_jpa.dto.response;

import com.pkuo.spring_data_jpa.dto.ResumeDTO;

import java.util.List;

public record GetResumePayload(List<ResumeDTO> resumeList) {

}
