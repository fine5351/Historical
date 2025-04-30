package com.fine_kuo.mybatisflex_core.dto.request;

import com.fine_kuo.mybatisflex_core.entity.Employee;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    public String name;
    public String address;
    public String gender;
    public String rocId;

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setAddress(address);
        employee.setGender(gender);
        employee.setRocId(rocId);
        return employee;
    }

}
