package com.finekuo.springdatajpa.controller;

import com.finekuo.normalcore.dto.request.CreateEmployeeRequest;
import com.finekuo.normalcore.util.Gsons;
import com.finekuo.springdatajpa.dto.response.GetMaskEmployeeResponse;
import com.finekuo.springdatajpa.entity.Employee;
import com.finekuo.springdatajpa.service.EmployeeService;
import com.finekuo.springdatajpa.service.EntityColumnMaskFlattenedService;
import com.finekuo.springdatajpa.service.EntityColumnMaskStructureService;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EntityColumnMaskStructureService entityColumnMaskStructureService;
    private final EntityColumnMaskFlattenedService entityColumnMaskFlattenedService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id,
            @RequestHeader(value = "account", required = false) String account,
            HttpServletRequest httpServletRequest
    ) {
        String method = httpServletRequest.getMethod();
        Employee employee = employeeService.findById(id);
        String uri = httpServletRequest.getRequestURI();
        log.info("Request URI: {}", uri);
        log.info("Account: {}", account);
        JsonObject jsonObject = Gsons.toJsonTree(employee).getAsJsonObject();
        JsonObject masked = entityColumnMaskStructureService.mask(account, method, uri, jsonObject);
        Employee maskedEmployee = Gsons.fromJson(masked, Employee.class);
        return ResponseEntity.ok(maskedEmployee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.save(toEntity(request)));
    }

    private Employee toEntity(CreateEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setAddress(request.getAddress());
        employee.setGender(request.getGender());
        employee.setRocId(request.getRocId());
        return employee;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return ResponseEntity.ok(employeeService.update(employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mask/{id}")
    public ResponseEntity<GetMaskEmployeeResponse> getMaskEmployeeById(
            @PathVariable Long id,
            @RequestHeader(value = "account", required = false) String account,
            HttpServletRequest httpServletRequest
    ) {
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        log.info("Request URI: {}", uri);
        log.info("Account: {}", account);
        Employee employee = employeeService.findById(id);
        GetMaskEmployeeResponse response = new GetMaskEmployeeResponse(employee);
        JsonObject jsonObject = Gsons.toJsonTree(response).getAsJsonObject();
        JsonObject masked = entityColumnMaskStructureService.mask(account, method, uri, jsonObject);
        response = Gsons.fromJson(masked, GetMaskEmployeeResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mask/flattened/{id}")
    public ResponseEntity<GetMaskEmployeeResponse> getMaskEmployeeFlattenedById(
            @PathVariable Long id,
            @RequestHeader(value = "account", required = false) String account,
            HttpServletRequest httpServletRequest
    ) {
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        log.info("Request URI: {}", uri);
        log.info("Account: {}", account);
        Employee employee = employeeService.findById(id);
        GetMaskEmployeeResponse response = new GetMaskEmployeeResponse(employee);
        JsonObject jsonObject = Gsons.toJsonTree(response).getAsJsonObject();
        JsonObject masked = entityColumnMaskFlattenedService.mask(account, method, uri, jsonObject);
        response = Gsons.fromJson(masked, GetMaskEmployeeResponse.class);
        return ResponseEntity.ok(response);
    }

}