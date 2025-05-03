package com.finekuo.springdatajpa.service;

import com.finekuo.springdatajpa.entity.Employee;
import com.finekuo.springdatajpa.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new RuntimeException("Employee not found with id: " + employee.getId());
        }
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
