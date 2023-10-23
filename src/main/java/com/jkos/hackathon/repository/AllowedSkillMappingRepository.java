package com.jkos.hackathon.repository;

import com.jkos.hackathon.entity.AllowedSkillMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedSkillMappingRepository extends CrudRepository<AllowedSkillMapping, Long> {
}
