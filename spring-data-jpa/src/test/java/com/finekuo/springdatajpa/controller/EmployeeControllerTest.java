package com.finekuo.springdatajpa.controller;

import com.finekuo.springdatajpa.entity.Employee;
import com.finekuo.springdatajpa.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
class EmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Test
    void getAllEmployees() {
        // Arrange
        Employee employee = new Employee();
        employee.setName("John Doe");
        employeeService.save(employee);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity("/employee", List.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void getEmployeeById() {
        // Arrange
        Employee employee = new Employee();
        employee.setName("Jane Doe");
        Employee savedEmployee = employeeService.save(employee);

        // Act
        ResponseEntity<Employee> response = restTemplate.getForEntity("/employee/" + savedEmployee.getId(), Employee.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void createEmployee() {
        // Arrange
        Employee employee = new Employee();
        employee.setName("New Employee");

        // Act
        ResponseEntity<Employee> response = restTemplate.postForEntity("/employee", employee, Employee.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Employee");
    }

    @Test
    void updateEmployee() {
        // Arrange
        Employee employee = new Employee();
        employee.setName("Old Name");
        Employee savedEmployee = employeeService.save(employee);

        savedEmployee.setName("Updated Name");

        // Act
        restTemplate.put("/employee/" + savedEmployee.getId(), savedEmployee);

        // Assert
        Employee updatedEmployee = employeeService.findById(savedEmployee.getId());
        assertThat(updatedEmployee.getName()).isEqualTo("Updated Name");
    }

    @Test
    void deleteEmployee() {
        // Arrange
        Employee employee = new Employee();
        employee.setName("To Be Deleted");
        Employee savedEmployee = employeeService.save(employee);

        // Act
        restTemplate.delete("/employee/" + savedEmployee.getId());

        // Assert
        assertThat(employeeService.findAll()).doesNotContain(savedEmployee);
    }

}
