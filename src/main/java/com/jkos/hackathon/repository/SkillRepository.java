package com.jkos.hackathon.repository;

import com.jkos.hackathon.entity.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {

    List<Skill> findAll();

}
