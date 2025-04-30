package com.finekuo.springdatajpa.repository;

import com.finekuo.springdatajpa.entity.Vacancy;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {

}
