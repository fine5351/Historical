package com.finekuo.normalcore.dto.request;

import lombok.Data;

@Data
public class CreateEmployeeRequest {

    public String name;
    public String address;
    public String gender;
    public String rocId;

}
