package com.jkos.hackathon.repository;

import com.jkos.hackathon.entity.Team;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

}
