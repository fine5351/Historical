package com.pkuo.shardingsphereproxy.mapper;

import com.mybatisflex.core.BaseMapper;
import com.pkuo.shardingsphereproxy.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("SELECT * FROM hackathon.employee WHERE id = #{id}")
    Employee selectOneByIdFromHackathon(Long id);

}