package com.finekuo.springdatajpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.normalcore.dto.request.CreateEmployeeRequest; // Corrected import
import com.finekuo.springdatajpa.entity.Employee;
import com.finekuo.springdatajpa.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult; // Added MvcResult import
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // Ensures test data isolation and rollback
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Removed @MockBean for EmployeeService

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll(); // Clear data before each test
    }

    @Test
    public void getAllEmployees_shouldReturnListOfEmployees() throws Exception {
        Employee emp1 = new Employee();
        emp1.setName("John Doe");
        emp1.setAddress("123 IT Street"); // Using address as per normal-core DTO
        // emp1.setDepartment("IT"); // Department not in CreateEmployeeRequest
        // emp1.setSalary(70000.0); // Salary not in CreateEmployeeRequest

        Employee emp2 = new Employee();
        emp2.setName("Jane Smith");
        emp2.setAddress("456 HR Avenue");
        // emp2.setDepartment("HR");
        // emp2.setSalary(60000.0);

        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    public void getEmployeeById_shouldReturnEmployee_whenFound() throws Exception {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setAddress("123 IT Street");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(get("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.address", is("123 IT Street")));
    }

    @Test
    public void getEmployeeById_shouldReturnNotFound_whenNotFound() throws Exception {
        mockMvc.perform(get("/employee/9999") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createEmployee_shouldReturnSavedEmployeeAndPersist() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("John Doe");
        request.setAddress("123 Tech Road");
        request.setGender("MALE"); // Assuming String for gender as per normal-core DTO
        request.setRocId("A123456789");

        MvcResult result = mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Assuming 200 OK for create
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.address", is("123 Tech Road")))
                .andExpect(jsonPath("$.rocId", is("A123456789"))) // RocIdConvert should handle this
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        Employee createdEmployee = objectMapper.readValue(responseBody, Employee.class);
        assertThat(createdEmployee.getId()).isNotNull();

        Optional<Employee> persistedEmployee = employeeRepository.findById(createdEmployee.getId());
        assertThat(persistedEmployee).isPresent();
        assertThat(persistedEmployee.get().getName()).isEqualTo(request.getName());
        assertThat(persistedEmployee.get().getAddress()).isEqualTo(request.getAddress());
        // RocId is converted, so we check the original value used for conversion.
        // The actual stored value might be encrypted/transformed based on RocIdConvert.
        // For simplicity, we'll assume the returned entity from controller has the original rocId or a retrievable form
        // For actual verification of converted value, one might need to know the conversion logic or inject the converter.
        assertThat(persistedEmployee.get().getRocId()).isEqualTo("A12*****89"); // Example if RocIdConvert masks it
    }


    @Test
    public void updateEmployee_shouldUpdateExistingEmployee() throws Exception {
        Employee initialEmployee = new Employee();
        initialEmployee.setName("Old Name");
        initialEmployee.setAddress("Old Address");
        initialEmployee.setRocId("A111111111"); // Original ROC ID
        Employee savedEmployee = employeeRepository.save(initialEmployee);

        Employee employeeUpdatePayload = new Employee(); // This is what the controller @RequestBody expects
        employeeUpdatePayload.setName("New Name");
        employeeUpdatePayload.setAddress("New Address");
        employeeUpdatePayload.setRocId("A222222222"); // New ROC ID for update

        mockMvc.perform(put("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.address", is("New Address")))
                .andExpect(jsonPath("$.rocId", is("A222222222"))); // Assuming RocIdConvert is idempotent or handles updates

        Optional<Employee> updatedEmployeeFromDb = employeeRepository.findById(savedEmployee.getId());
        assertThat(updatedEmployeeFromDb).isPresent();
        assertThat(updatedEmployeeFromDb.get().getName()).isEqualTo("New Name");
        assertThat(updatedEmployeeFromDb.get().getAddress()).isEqualTo("New Address");
        assertThat(updatedEmployeeFromDb.get().getRocId()).isEqualTo("A22*****22"); // Example if RocIdConvert masks it
    }
    
    @Test
    public void updateEmployee_shouldReturnNotFound_whenEmployeeDoesNotExist() throws Exception {
        Employee employeeUpdatePayload = new Employee();
        employeeUpdatePayload.setName("Non Existent");
        employeeUpdatePayload.setAddress("No Where");

        mockMvc.perform(put("/employee/99999") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdatePayload)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee_shouldRemoveEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("To Be Deleted");
        employee.setAddress("1 Delete Lane");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(delete("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(savedEmployee.getId())).isEmpty();
    }

    @Test
    public void deleteEmployee_shouldReturnNotFound_whenEmployeeDoesNotExist() throws Exception {
        // The service's delete method might throw EmptyResultDataAccessException if not found,
        // which Spring's default handler converts to 404.
        // If custom exception handling is in place, this might need adjustment.
        mockMvc.perform(delete("/employee/88888") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expecting 404 due to service-level handling or default exception translation
    }
}
