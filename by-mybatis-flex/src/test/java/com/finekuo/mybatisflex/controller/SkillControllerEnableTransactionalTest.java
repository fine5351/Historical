package com.finekuo.mybatisflex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.mybatisflexcore.entity.Skill;
import com.finekuo.mybatisflexcore.mapper.SkillMapper;
import com.finekuo.mybatisflex.BaseControllerEnableTransactionalTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SkillControllerEnableTransactionalTest extends BaseControllerEnableTransactionalTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SkillMapper skillMapper;

    // 預期的初始技能數量，根據 data.sql 設定
    private static final int INITIAL_SKILL_COUNT = 3;
    private static final Long EXISTING_SKILL_ID_1 = 1L;
    private static final Long NON_EXISTING_SKILL_ID = 999L;

    @Test
    @Order(1)
    void getAllSkills_shouldReturnAllSkills() throws Exception {
        mockMvc.perform(get("/api/skills")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", hasSize(INITIAL_SKILL_COUNT)));
    }

    @Test
    @Order(2)
    void getSkillById_whenExists_shouldReturnSkill() throws Exception {
        // 先創建一個技能以確保有數據可供測試
        Skill newSkill = new Skill();
        newSkill.setName("Java Programming");
        skillMapper.insert(newSkill);
        
        mockMvc.perform(get("/api/skills/" + newSkill.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", is(newSkill.getId().intValue())))
                .andExpect(jsonPath("$.data.name", is("Java Programming")));
    }

    @Test
    @Order(3)
    void getSkillById_whenNotExists_shouldReturnNullData() throws Exception {
        mockMvc.perform(get("/api/skills/" + NON_EXISTING_SKILL_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", is(nullValue())));
    }

    @Test
    @Order(4)
    void createSkill_shouldCreateAndReturnSkillWithId() throws Exception {
        Skill newSkill = new Skill();
        newSkill.setName("Python Programming");

        mockMvc.perform(post("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSkill))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name", is("Python Programming")))
                .andDo(result -> { // 驗證資料庫中的數據
                    String responseString = result.getResponse().getContentAsString();
                    Skill createdSkill = objectMapper.readValue(
                            objectMapper.readTree(responseString).get("data").toString(),
                            Skill.class);
                    assertNotNull(createdSkill.getId(), "創建的技能ID不應為空");
                    Skill dbSkill = skillMapper.selectOneById(createdSkill.getId());
                    assertNotNull(dbSkill, "創建後技能應存在於資料庫中");
                    assertEquals("Python Programming", dbSkill.getName());
                });

        // 驗證數量增加
        mockMvc.perform(get("/api/skills")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(INITIAL_SKILL_COUNT + 1)));
    }

    @Test
    @Order(5)
    void updateSkill_shouldUpdateAndReturnSkill() throws Exception {
        // 先創建一個技能以確保有數據可供更新
        Skill skillToUpdate = new Skill();
        skillToUpdate.setName("Initial Skill");
        skillMapper.insert(skillToUpdate);
        
        Skill updatedSkill = new Skill();
        updatedSkill.setName("Updated Skill");

        mockMvc.perform(put("/api/skills/" + skillToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSkill))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", is(skillToUpdate.getId().intValue())))
                .andExpect(jsonPath("$.data.name", is("Updated Skill")));

        // 驗證資料庫中的數據
        Skill dbSkill = skillMapper.selectOneById(skillToUpdate.getId());
        assertNotNull(dbSkill);
        assertEquals("Updated Skill", dbSkill.getName());
    }

    @Test
    @Order(6)
    void deleteSkill_shouldRemoveSkill() throws Exception {
        // 先創建一個技能以確保有數據可供刪除
        Skill skillToDelete = new Skill();
        skillToDelete.setName("Skill to Delete");
        skillMapper.insert(skillToDelete);

        mockMvc.perform(delete("/api/skills/" + skillToDelete.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")));

        // 驗證已從資料庫中刪除
        Skill dbSkill = skillMapper.selectOneById(skillToDelete.getId());
        assertNull(dbSkill, "刪除後技能應在資料庫中為空");
    }
}
