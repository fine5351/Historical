package com.finekuo.byshardingspherejdbc.controller;

import com.finekuo.mybatisflexcore.entity.Employee;
import com.finekuo.mybatisflexcore.mapper.EmployeeMapper;
import com.finekuo.normalcore.dto.response.BaseResponse;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<Employee>> getAllEmployees() {
        return BaseResponse.success(employeeMapper.selectAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    public BaseResponse<Employee> getEmployeeById(@PathVariable Long id) {
        return BaseResponse.success(employeeMapper.selectOneById(id));
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Creates a new employee")
    public BaseResponse<Employee> createEmployee(@RequestBody Employee employee) {
        employeeMapper.insert(employee);
        return BaseResponse.success(employee);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    public BaseResponse<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        employeeMapper.update(employee);
        return BaseResponse.success(employee);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Deletes an employee by their ID")
    public BaseResponse<Void> deleteEmployee(@PathVariable Long id) {
        employeeMapper.deleteById(id);
        return BaseResponse.success();
    }

    @GetMapping("/page")
    @Operation(summary = "Get employees with pagination", description = "Retrieves a paginated list of employees")
    public BaseResponse<Page<Employee>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return BaseResponse.success(employeeMapper.paginate(pageNumber, pageSize, select()));
    }

}
