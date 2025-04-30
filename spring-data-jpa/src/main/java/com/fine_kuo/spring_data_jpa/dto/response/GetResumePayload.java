package com.fine_kuo.spring_data_jpa.dto.response;

import com.fine_kuo.spring_data_jpa.dto.ResumeDTO;

import java.util.List;

public record GetResumePayload(List<ResumeDTO> resumeList) {

}
