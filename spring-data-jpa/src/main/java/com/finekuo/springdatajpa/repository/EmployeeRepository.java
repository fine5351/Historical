package com.finekuo.springdatajpa.repository;

import com.finekuo.springdatajpa.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
