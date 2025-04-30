package com.fine_kuo.by_shardingsphere_jdbc.mapper;

import com.fine_kuo.by_shardingsphere_jdbc.entity.Employee;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    // BaseMapper provides basic CRUD operations
    // You can add custom methods here if needed
}