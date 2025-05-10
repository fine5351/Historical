package com.finekuo.springdatajpa.repository;

import com.finekuo.springdatajpa.entity.AllowedSkillMapping;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Hidden
@Repository
public interface AllowedSkillMappingRepository extends CrudRepository<AllowedSkillMapping, Long> {

    @Transactional
    @Modifying
    @Query(value = "truncate table ALLOWED_SKILL_MAPPING", nativeQuery = true)
    void truncate();

}
