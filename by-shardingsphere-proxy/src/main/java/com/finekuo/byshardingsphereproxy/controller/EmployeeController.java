package com.finekuo.byshardingsphereproxy.controller;

import com.finekuo.mybatisflexcore.dto.request.CreateEmployeeRequest;
import com.finekuo.mybatisflexcore.entity.Employee;
import com.finekuo.mybatisflexcore.mapper.EmployeeMapper;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.select;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee API", description = "Employee management APIs")
public class EmployeeController {

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    public List<Employee> getAllEmployees() {
        return employeeMapper.selectAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeMapper.selectOneById(id);
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Creates a new employee")
    public Employee createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee employee = request.toEmployee();
        employeeMapper.insert(employee);
        return employee;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        employeeMapper.update(employee);
        return employee;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Deletes an employee by their ID")
    public void deleteEmployee(@PathVariable Long id) {
        employeeMapper.deleteById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "Get employees with pagination", description = "Retrieves a paginated list of employees")
    public Page<Employee> getEmployeesWithPagination(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return employeeMapper.paginate(pageNumber, pageSize, select());
    }

}