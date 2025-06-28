package com.finekuo.mybatisflexcore.mapper;

import com.finekuo.mybatisflexcore.entity.Employee;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 員工映射器介面，對應於資料庫中的 employee 表格
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


}
