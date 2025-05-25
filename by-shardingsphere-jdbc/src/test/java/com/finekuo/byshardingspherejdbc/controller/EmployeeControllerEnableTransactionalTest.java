package com.finekuo.byshardingspherejdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.mybatisflexcore.entity.Employee;
import com.finekuo.mybatisflexcore.mapper.EmployeeMapper;
import com.finekuo.normalcore.BaseControllerEnableTransactionalTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EmployeeControllerEnableTransactionalTest extends BaseControllerEnableTransactionalTest {

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
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", hasSize(INITIAL_EMPLOYEE_COUNT)))
                .andExpect(jsonPath("$.data[0].name", is("Alice Smith"))); // Check first employee from data.sql
    }

    @Test
    @Order(2)
    void getEmployeeById_whenExists_shouldReturnEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/" + EXISTING_EMPLOYEE_ID_1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", is(EXISTING_EMPLOYEE_ID_1.intValue())))
                .andExpect(jsonPath("$.data.name", is("Alice Smith")))
                .andExpect(jsonPath("$.data.address", is("123 Maple St")));
    }

    @Test
    @Order(3)
    void getEmployeeById_whenNotExists_shouldReturnNullData() throws Exception {
        mockMvc.perform(get("/api/employees/" + NON_EXISTING_EMPLOYEE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", is(nullValue())));
    }

    @Test
    @Order(4)
    void createEmployee_shouldCreateAndReturnEmployeeWithId() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setName("David Lee");
        newEmployee.setAddress("789 Oak St");
        newEmployee.setGender("Male");
        newEmployee.setRocId("D123456789");
        // BaseEntity fields - normally set by framework or service layer,
        // but for direct controller test, we might need to set them if required by DB constraints
        // For H2 with defaults, these might not be strictly needed in the request
        // newEmployee.setCreatedBy("test-user");
        // newEmployee.setUpdatedBy("test-user");
        // newEmployee.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        // newEmployee.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));


        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name", is("David Lee")))
                .andDo(result -> { // Verify in DB
                    String responseString = result.getResponse().getContentAsString();
                    Employee createdEmployee = objectMapper.readValue(
                            objectMapper.readTree(responseString).get("data").toString(),
                            Employee.class);
                    assertNotNull(createdEmployee.getId(), "Created employee ID should not be null");
                    Employee dbEmployee = employeeMapper.selectOneById(createdEmployee.getId());
                    assertNotNull(dbEmployee, "Employee should exist in DB after creation");
                    assertEquals("David Lee", dbEmployee.getName());
                });

        // Verify count increased
        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(INITIAL_EMPLOYEE_COUNT + 1)));
    }

    @Test
    @Order(5)
    void updateEmployee_shouldUpdateAndReturnEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Alice Johnson (Updated)");
        updatedEmployee.setAddress("123 Maple St Updated");
        updatedEmployee.setGender("Female"); // Assuming gender remains same
        updatedEmployee.setRocId("A123456789"); // Assuming ROC ID remains same or can be updated

        mockMvc.perform(put("/api/employees/" + EXISTING_EMPLOYEE_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.id", is(EXISTING_EMPLOYEE_ID_1.intValue())))
                .andExpect(jsonPath("$.data.name", is("Alice Johnson (Updated)")))
                .andExpect(jsonPath("$.data.address", is("123 Maple St Updated")));

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
                .andExpect(jsonPath("$.message", is("success")));

        // Verify deleted from DB
        Employee dbEmployee = employeeMapper.selectOneById(EXISTING_EMPLOYEE_ID_2);
        assertNull(dbEmployee, "Employee should be null in DB after deletion");

        // Verify count decreased
        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(INITIAL_EMPLOYEE_COUNT - 1)));
    }

    @Test
    @Order(7)
    void getEmployeesWithPagination_defaultParams_shouldReturnPagedResult() throws Exception {
        // After potential create/delete, let's ensure we fetch current state for pagination
        // For simplicity, this test assumes it runs after the previous ones in this specific order.
        // A more robust test would setup its own data or query based on a known state.
        // Let's assume at this point we have (INITIAL_EMPLOYEE_COUNT - 1 + 1) = INITIAL_EMPLOYEE_COUNT employees.
        // If create and delete happened: 3 - 1 (deleted Bob) + 1 (created David) = 3 employees.

        mockMvc.perform(get("/api/employees/page")
                        .param("pageNumber", "1")
                        .param("pageSize", "2") // Request 2 items for page 1
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.pageNumber", is(1)))
                .andExpect(jsonPath("$.data.pageSize", is(2)))
                .andExpect(jsonPath("$.data.totalRow", is(INITIAL_EMPLOYEE_COUNT))) // Total employees in DB
                .andExpect(jsonPath("$.data.records", hasSize(2))); // Records on current page
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
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.pageNumber", is(2)))
                .andExpect(jsonPath("$.data.pageSize", is(2)))
                .andExpect(jsonPath("$.data.totalRow", is(INITIAL_EMPLOYEE_COUNT)))
                .andExpect(jsonPath("$.data.records", hasSize(1))); // Remaining record on page 2
    }

}
