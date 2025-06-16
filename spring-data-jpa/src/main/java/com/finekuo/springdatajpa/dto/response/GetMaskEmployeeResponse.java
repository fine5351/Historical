package com.finekuo.springdatajpa.dto.response;

import com.finekuo.springdatajpa.entity.Employee;
import lombok.Data;

@Data
public class GetMaskEmployeeResponse {

    private Long id;
    private String name;
    private String address;
    private String gender;
    private String rocId;
    private NestedData nestedData;

    public GetMaskEmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.address = employee.getAddress();
        this.gender = employee.getGender();
        this.rocId = employee.getRocId();
        this.nestedData = new NestedData(employee);
    }

    @Data
    public static class NestedData {
        private Long id;
        private String name;
        private String rocId;

        public NestedData(Employee employee) {
            this.id = employee.getId();
            this.name = employee.getName();
            this.rocId = employee.getRocId();
        }
    }
}
