package com.jkos.hackathon.repository;

import com.jkos.hackathon.entity.Vacancy;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {

}
