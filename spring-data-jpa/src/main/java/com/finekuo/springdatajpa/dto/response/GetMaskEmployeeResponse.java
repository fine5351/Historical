package com.finekuo.springdatajpa.dto.response;

import com.finekuo.springdatajpa.entity.Employee;
import lombok.Data;

@Data
public class GetMaskEmployeeResponse {

    private ParentNode parentNode;
    private EmployeeDTO employeeDTO;

    public GetMaskEmployeeResponse(Employee employee) {
        this.parentNode = new ParentNode();
        this.parentNode.setId(employee.getId());
        this.parentNode.setName(employee.getName());
        this.parentNode.setChildEmployeeDTO(new ParentNode.ChildEmployeeDTO(employee));
        this.employeeDTO = new EmployeeDTO(employee);
    }

    @Data
    public static class ParentNode {

        private Long id;
        private String name;
        private ChildEmployeeDTO childEmployeeDTO;

        @Data
        public static class ChildEmployeeDTO {

            private Long id;
            private String name;
            private String rocId;

            public ChildEmployeeDTO(Employee employee) {
                this.id = employee.getId();
                this.name = employee.getName();
                this.rocId = employee.getRocId();
            }

        }

    }

    @Data
    public static class EmployeeDTO {

        private Long id;
        private String name;

        public EmployeeDTO(Employee employee) {
            this.id = employee.getId();
            this.name = employee.getName();
        }

    }

}
