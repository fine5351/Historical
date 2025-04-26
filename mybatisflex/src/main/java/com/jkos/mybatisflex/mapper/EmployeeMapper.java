package com.jkos.mybatisflex.mapper;

import com.jkos.mybatisflex.entity.Employee;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    // BaseMapper provides basic CRUD operations
    // You can add custom methods here if needed
}