package com.finekuo.mybatisflex.controller;

import com.finekuo.mybatisflexcore.entity.Skill;
import com.finekuo.mybatisflexcore.mapper.SkillMapper;
import com.finekuo.normalcore.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "技能 API", description = "技能管理 APIs")
public class SkillController {

    @Autowired
    private SkillMapper skillMapper;

    @GetMapping
    @Operation(summary = "獲取所有技能", description = "檢索所有技能的列表")
    public BaseResponse<List<Skill>> getAllSkills() {
        return BaseResponse.success(skillMapper.selectAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根據ID獲取技能", description = "根據技能ID檢索單個技能")
    public BaseResponse<Skill> getSkillById(@PathVariable Long id) {
        return BaseResponse.success(skillMapper.selectOneById(id));
    }

    @PostMapping
    @Operation(summary = "創建技能", description = "創建一個新的技能")
    public BaseResponse<Skill> createSkill(@RequestBody Skill skill) {
        skillMapper.insert(skill);
        return BaseResponse.success(skill);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新技能", description = "更新現有的技能")
    public BaseResponse<Skill> updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        skill.setId(id);
        skillMapper.update(skill);
        return BaseResponse.success(skill);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除技能", description = "根據技能ID刪除技能")
    public BaseResponse<Void> deleteSkill(@PathVariable Long id) {
        skillMapper.deleteById(id);
        return BaseResponse.success();
    }
}
