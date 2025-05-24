package com.finekuo.springdatajpa.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.normalcore.constant.ResumeStatus; // Corrected import
import com.finekuo.normalcore.dto.response.BaseResponse; // Corrected import
import com.finekuo.springdatajpa.dto.ResumeDTO;
import com.finekuo.springdatajpa.dto.response.GetResumePayload; // Corrected import
import com.finekuo.springdatajpa.entity.Resume;
import com.finekuo.springdatajpa.repository.ResumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator; // Added import
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport; // Added import

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResumeRepository resumeRepository; // Injected for E2E

    // Removed @MockBean for ResumeRepository

    @BeforeEach
    public void setUp() {
        resumeRepository.deleteAll();
    }

    @Test
    public void getResume_shouldReturnListOfResumes() throws Exception {
        Resume resume1 = new Resume();
        resume1.setFileName("resume1.pdf");
        // resume1.setS3Url("s3://bucket/resume1.pdf"); // S3Url is set by controller/service
        resume1.setStatus(ResumeStatus.PENDING);
        // createdAt and updatedAt are set by BaseEntity auditing

        Resume resume2 = new Resume();
        resume2.setFileName("resume2.docx");
        // resume2.setS3Url("s3://bucket/resume2.docx");
        resume2.setStatus(ResumeStatus.PROCESSED);
        // createdAt and updatedAt are set by BaseEntity auditing

        // Save entities to get IDs and audit dates populated
        List<Resume> savedResumes = StreamSupport.stream(resumeRepository.saveAll(Arrays.asList(resume1, resume2)).spliterator(), false)
                                        .collect(Collectors.toList());


        MvcResult result = mockMvc.perform(get("/resume/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<GetResumePayload> response = objectMapper.readValue(contentAsString, new TypeReference<BaseResponse<GetResumePayload>>() {});

        assertThat(response.getCode()).isEqualTo(com.finekuo.normalcore.constant.ResponseStatusCode.SUCCESS.getCode());
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().resumeList()).hasSize(2); // Changed to record accessor

        // Convert saved entities (which now have IDs and audit dates) to DTOs for comparison
        List<ResumeDTO> expectedDtos = savedResumes.stream()
                .map(ResumeDTO::fromEntity) // Assumes ResumeDTO.fromEntity handles this
                .collect(Collectors.toList());
        
        // Sort by filename for consistent comparison order if not guaranteed by retrieval
        List<ResumeDTO> actualDtos = response.getData().resumeList(); // Changed to record accessor
        actualDtos.sort(Comparator.comparing(ResumeDTO::getFileName));
        expectedDtos.sort(Comparator.comparing(ResumeDTO::getFileName));

        // Compare relevant fields. ID, createdAt, updatedAt are generated.
        // fileName and status (ordinal) are key.
        for (int i = 0; i < expectedDtos.size(); i++) {
            ResumeDTO expected = expectedDtos.get(i);
            ResumeDTO actual = actualDtos.get(i);
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getFileName()).isEqualTo(expected.getFileName());
            assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
            // Timestamps can be tricky; assert they are not null or compare with tolerance if needed
            assertThat(actual.getCreatedAt()).isNotNull();
            assertThat(actual.getUpdatedAt()).isNotNull();
        }
    }

    @Test
    public void createResume_shouldSaveResumeAndReturnSuccessAndPersist() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", // parameter name in the controller
                "test-e2e-resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "This is an E2E test resume content.".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/resume/")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andReturn();

        // Assert response
        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<Void> response = objectMapper.readValue(contentAsString, new TypeReference<BaseResponse<Void>>() {});

        assertThat(response.getCode()).isEqualTo(com.finekuo.normalcore.constant.ResponseStatusCode.SUCCESS.getCode());
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNull();

        // Verify persistence in H2
        Optional<Resume> persistedResumeOpt = resumeRepository.findByFileName(mockFile.getOriginalFilename());
        assertThat(persistedResumeOpt).isPresent();
        Resume persistedResume = persistedResumeOpt.get();
        assertThat(persistedResume.getFileName()).isEqualTo(mockFile.getOriginalFilename());
        assertThat(persistedResume.getStatus()).isEqualTo(ResumeStatus.PENDING);
        // assertThat(persistedResume.getS3Url()).isNotNull(); // Resume entity does not have s3Url field. Controller logic for this needs review if it's expected.
        assertThat(persistedResume.getCreatedAt()).isNotNull(); // from BaseEntity
        assertThat(persistedResume.getUpdatedAt()).isNotNull(); // from BaseEntity
    }
}
