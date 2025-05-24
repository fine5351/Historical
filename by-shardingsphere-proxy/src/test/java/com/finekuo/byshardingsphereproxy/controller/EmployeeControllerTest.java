package com.finekuo.byshardingsphereproxy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.mybatisflexcore.entity.Employee;
import com.finekuo.mybatisflexcore.mapper.EmployeeMapper;
import com.finekuo.normalcore.dto.request.CreateEmployeeRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure each test runs in a transaction and rolls back
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    // Expected initial employee count from data.sql
    private static final int INITIAL_EMPLOYEE_COUNT = 3;
    private static final Long EXISTING_EMPLOYEE_ID_1 = 1L;
    private static final Long EXISTING_EMPLOYEE_ID_2 = 2L;
    private static final Long NON_EXISTING_EMPLOYEE_ID = 999L;

    @Test
    @Order(1)
    void getAllEmployees_shouldReturnAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(INITIAL_EMPLOYEE_COUNT)))
                .andExpect(jsonPath("$[0].name", is("Alice Smith"))); // Check first employee from data.sql
    }

    @Test
    @Order(2)
    void getEmployeeById_whenExists_shouldReturnEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/" + EXISTING_EMPLOYEE_ID_1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXISTING_EMPLOYEE_ID_1.intValue())))
                .andExpect(jsonPath("$.name", is("Alice Smith")))
                .andExpect(jsonPath("$.address", is("123 Maple St")));
    }

    @Test
    @Order(3)
    void getEmployeeById_whenNotExists_shouldReturnEmptyBody() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/employees/" + NON_EXISTING_EMPLOYEE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Or .isNotFound() if controller is changed to throw exception
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.isEmpty() || content.equals("null"), "Response body should be empty or 'null' for non-existing ID");
    }

    @Test
    @Order(4)
    void createEmployee_shouldCreateAndReturnEmployeeWithId() throws Exception {
        CreateEmployeeRequest createRequest = new CreateEmployeeRequest();
        createRequest.setName("David Lee");
        createRequest.setAddress("789 Oak St");
        createRequest.setGender("Male");
        createRequest.setRocId("D123456789");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Assuming 200 OK based on controller
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("David Lee")))
                .andDo(result -> { // Verify in DB
                    String responseString = result.getResponse().getContentAsString();
                    Employee createdEmployee = objectMapper.readValue(responseString, Employee.class);
                    assertNotNull(createdEmployee.getId(), "Created employee ID should not be null");
                    Employee dbEmployee = employeeMapper.selectOneById(createdEmployee.getId());
                    assertNotNull(dbEmployee, "Employee should exist in DB after creation");
                    assertEquals("David Lee", dbEmployee.getName());
                });

        // Verify count increased
        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(INITIAL_EMPLOYEE_COUNT + 1)));
    }

    @Test
    @Order(5)
    void updateEmployee_shouldUpdateAndReturnEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        // ID is set by @PathVariable in controller, not from this body's ID field for update mapping.
        // However, the controller's method employee.setId(id) sets it.
        // The returned object from controller method `updateEmployee` will have the ID.
        updatedEmployee.setName("Alice Johnson (Updated)");
        updatedEmployee.setAddress("123 Maple St Updated");
        updatedEmployee.setGender("Female");
        updatedEmployee.setRocId("A123456789");

        mockMvc.perform(put("/api/employees/" + EXISTING_EMPLOYEE_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXISTING_EMPLOYEE_ID_1.intValue())))
                .andExpect(jsonPath("$.name", is("Alice Johnson (Updated)")))
                .andExpect(jsonPath("$.address", is("123 Maple St Updated")));

        // Verify in DB
        Employee dbEmployee = employeeMapper.selectOneById(EXISTING_EMPLOYEE_ID_1);
        assertNotNull(dbEmployee);
        assertEquals("Alice Johnson (Updated)", dbEmployee.getName());
        assertEquals("123 Maple St Updated", dbEmployee.getAddress());
    }

    @Test
    @Order(6)
    void deleteEmployee_shouldRemoveEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/" + EXISTING_EMPLOYEE_ID_2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // Expecting empty body for void return

        // Verify deleted from DB
        Employee dbEmployee = employeeMapper.selectOneById(EXISTING_EMPLOYEE_ID_2);
        assertNull(dbEmployee, "Employee should be null in DB after deletion");

        // Verify count decreased
        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(INITIAL_EMPLOYEE_COUNT - 1)));
    }

    @Test
    @Order(7)
    void getEmployeesWithPagination_defaultParams_shouldReturnPagedResult() throws Exception {
        // State after create & delete: INITIAL_EMPLOYEE_COUNT - 1 + 1 = INITIAL_EMPLOYEE_COUNT
        mockMvc.perform(get("/api/employees/page")
                        .param("pageNumber", "1")
                        .param("pageSize", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.pageSize", is(2)))
                .andExpect(jsonPath("$.totalRow", is(INITIAL_EMPLOYEE_COUNT)))
                .andExpect(jsonPath("$.list", hasSize(2)));
    }

    @Test
    @Order(8)
    void getEmployeesWithPagination_specificPage_shouldReturnCorrectSubset() throws Exception {
        // Assuming INITIAL_EMPLOYEE_COUNT = 3. Page 2, Size 2 should give 1 record.
        mockMvc.perform(get("/api/employees/page")
                        .param("pageNumber", "2")
                        .param("pageSize", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(2)))
                .andExpect(jsonPath("$.pageSize", is(2)))
                .andExpect(jsonPath("$.totalRow", is(INITIAL_EMPLOYEE_COUNT)))
                .andExpect(jsonPath("$.list", hasSize(1)));
    }
}
