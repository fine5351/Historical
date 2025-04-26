package com.jkos.hackathon.repository;

import com.jkos.hackathon.entity.AllowedSkillMapping;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Hidden
@Repository
public interface AllowedSkillMappingRepository extends CrudRepository<AllowedSkillMapping, Long> {

    List<AllowedSkillMapping> findAll();

    @Transactional
    @Modifying
    @Query(value = "truncate table ALLOWED_SKILL_MAPPING", nativeQuery = true)
    void truncate();

}
