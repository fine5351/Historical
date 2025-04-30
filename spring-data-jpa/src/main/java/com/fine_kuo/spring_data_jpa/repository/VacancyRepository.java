package com.fine_kuo.spring_data_jpa.repository;

import com.fine_kuo.spring_data_jpa.entity.Vacancy;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {

}
