package com.pkuo.springdatajpa.repository;

import com.pkuo.springdatajpa.entity.Resume;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface ResumeRepository extends CrudRepository<Resume, Long> {

    List<Resume> findAll();

}
