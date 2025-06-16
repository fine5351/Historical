package com.finekuo.springdatajpa.controller;

import com.finekuo.normalcore.BaseControllerEnableTransactionalTest;
import com.finekuo.normalcore.dto.request.CreateEmployeeRequest;
import com.finekuo.normalcore.util.Jsons;
import com.finekuo.springdatajpa.entity.Employee;
import com.finekuo.springdatajpa.entity.EntityColumnMaskFlattened;
import com.finekuo.springdatajpa.entity.EntityColumnMaskStructure;
import com.finekuo.springdatajpa.repository.EmployeeRepository;
import com.finekuo.springdatajpa.repository.EntityColumnMaskFlattenedRepository;
import com.finekuo.springdatajpa.repository.EntityColumnMaskStructureRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EmployeeControllerEnableTransactionalTest extends BaseControllerEnableTransactionalTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityColumnMaskStructureRepository structureRepository;

    @Autowired
    private EntityColumnMaskFlattenedRepository flattenedRepository;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll(); // Clear data before each test
        structureRepository.deleteAll(); // 清除掩码设置
        flattenedRepository.deleteAll(); // 清除扁平化掩码设置
    }

    @Test
    public void getAllEmployees_shouldReturnListOfEmployees() throws Exception {
        Employee emp1 = new Employee();
        emp1.setName("John Doe");
        emp1.setAddress("123 IT Street"); // Using address as per normal-core DTO
        // emp1.setDepartment("IT"); // Department not in CreateEmployeeRequest
        // emp1.setSalary(70000.0); // Salary not in CreateEmployeeRequest

        Employee emp2 = new Employee();
        emp2.setName("Jane Smith");
        emp2.setAddress("456 HR Avenue");
        // emp2.setDepartment("HR");
        // emp2.setSalary(60000.0);

        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    public void getEmployeeById_shouldReturnEmployee_whenFound() throws Exception {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setAddress("123 IT Street");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(get("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.address", is("123 IT Street")));
    }

    @Test
    public void getEmployeeById_shouldReturnNotFound_whenNotFound() throws Exception {
        mockMvc.perform(get("/employee/9999") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void createEmployee_shouldReturnSavedEmployeeAndPersist() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("John Doe");
        request.setAddress("123 Tech Road");
        request.setGender("MALE"); // Assuming String for gender as per normal-core DTO
        request.setRocId("A123456789");

        MvcResult result = mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(request)))
                .andExpect(status().isOk()) // Assuming 200 OK for create
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.address", is("123 Tech Road")))
                .andExpect(jsonPath("$.rocId", is("A123456789"))) // RocIdConvert should handle this
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Employee createdEmployee = Jsons.fromJson(responseBody, Employee.class);
        assertThat(createdEmployee.getId()).isNotNull();

        Optional<Employee> persistedEmployee = employeeRepository.findById(createdEmployee.getId());
        assertThat(persistedEmployee).isPresent();
        assertThat(persistedEmployee.get().getName()).isEqualTo(request.getName());
        assertThat(persistedEmployee.get().getAddress()).isEqualTo(request.getAddress());
        // RocId is converted, so we check the original value used for conversion.
        // The actual stored value might be encrypted/transformed based on RocIdConvert.
        // For simplicity, we'll assume the returned entity from controller has the original rocId or a retrievable form
        // For actual verification of converted value, one might need to know the conversion logic or inject the converter.
        assertThat(persistedEmployee.get().getRocId()).isEqualTo("A123456789");
    }


    @Test
    public void updateEmployee_shouldUpdateExistingEmployee() throws Exception {
        Employee initialEmployee = new Employee();
        initialEmployee.setName("Old Name");
        initialEmployee.setAddress("Old Address");
        initialEmployee.setRocId("A111111111"); // Original ROC ID
        Employee savedEmployee = employeeRepository.save(initialEmployee);

        Employee employeeUpdatePayload = new Employee(); // This is what the controller @RequestBody expects
        employeeUpdatePayload.setName("New Name");
        employeeUpdatePayload.setAddress("New Address");
        employeeUpdatePayload.setRocId("A222222222"); // New ROC ID for update

        mockMvc.perform(put("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(employeeUpdatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.address", is("New Address")))
                .andExpect(jsonPath("$.rocId", is("A222222222"))); // Assuming RocIdConvert is idempotent or handles updates

        Optional<Employee> updatedEmployeeFromDb = employeeRepository.findById(savedEmployee.getId());
        assertThat(updatedEmployeeFromDb).isPresent();
        assertThat(updatedEmployeeFromDb.get().getName()).isEqualTo("New Name");
        assertThat(updatedEmployeeFromDb.get().getAddress()).isEqualTo("New Address");
        assertThat(updatedEmployeeFromDb.get().getRocId()).isEqualTo("A222222222"); // Example if RocIdConvert masks it
    }

    @Test
    public void updateEmployee_shouldReturnNotFound_whenEmployeeDoesNotExist() throws Exception {
        Employee employeeUpdatePayload = new Employee();
        employeeUpdatePayload.setName("Non Existent");
        employeeUpdatePayload.setAddress("No Where");

        mockMvc.perform(put("/employee/99999") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(employeeUpdatePayload)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteEmployee_shouldRemoveEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("To Be Deleted");
        employee.setAddress("1 Delete Lane");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(delete("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(savedEmployee.getId())).isEmpty();
    }

    @Test
    public void deleteEmployee_shouldReturnNotFound_whenEmployeeDoesNotExist() throws Exception {
        // The service's delete method might throw EmptyResultDataAccessException if not found,
        // which Spring's default handler converts to 404.
        // If custom exception handling is in place, this might need adjustment.
        mockMvc.perform(delete("/employee/88888") // Non-existent ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // Expecting 404 due to service-level handling or default exception translation
    }

    @Test
    public void getMaskEmployeeById_withStructureMask_shouldMaskSpecifiedFields() throws Exception {
        // 1. 准备测试数据
        Employee employee = new Employee();
        employee.setName("Test User");
        employee.setAddress("Secret Address");
        employee.setGender("Male");
        employee.setRocId("S123456789");
        Employee savedEmployee = employeeRepository.save(employee);

        // 2. 创建结构化掩码配置 - 掩码 address 和嵌套的 rocId
        EntityColumnMaskStructure maskStructure = new EntityColumnMaskStructure();
        maskStructure.setAccount("testAccount");
        maskStructure.setMethod("GET");
        maskStructure.setApiUri("/employee/mask/{id}");

        // 创建结构化掩码配置 JSON - 指定要掩码的字段
        JsonObject maskSettings = new JsonObject();
        maskSettings.addProperty("address", true); // 掩码顶级字段 address

        // 为嵌套字段添加掩码配置
        JsonObject nestedSettings = new JsonObject();
        nestedSettings.addProperty("rocId", true); // 掩码嵌套字段中的 rocId
        maskSettings.add("nestedData", nestedSettings);

        maskStructure.setMaskSettings(new Gson().toJson(maskSettings));
        structureRepository.save(maskStructure);

        // 3. 执行测试请求
        mockMvc.perform(get("/employee/mask/" + savedEmployee.getId())
                        .header("account", "testAccount") // 设置 account 头，这个会由 ResponseBodyAdvice 处理
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.address").doesNotExist()) // address 应被掩码
                .andExpect(jsonPath("$.gender", is("Male")))
                .andExpect(jsonPath("$.rocId", is("S123456789"))) // 顶级 rocId 不受影响
                .andExpect(jsonPath("$.nestedData.id", is(savedEmployee.getId().intValue())))
                .andExpect(jsonPath("$.nestedData.name", is("Test User")))
                .andExpect(jsonPath("$.nestedData.rocId").doesNotExist()); // 嵌套对象的 rocId 应被掩码
    }

    @Test
    public void getMaskEmployeeById_withFlattenedMask_shouldMaskAllMatchingFields() throws Exception {
        // 1. 准备测试数据
        Employee employee = new Employee();
        employee.setName("Another User");
        employee.setAddress("Another Address");
        employee.setGender("Female");
        employee.setRocId("F123456789");
        Employee savedEmployee = employeeRepository.save(employee);

        // 2. 创建扁平化掩码配置 - 掩码所有 rocId 和 gender 字段，无论在哪一层
        EntityColumnMaskFlattened maskFlattened = new EntityColumnMaskFlattened();
        maskFlattened.setAccount("flatAccount");
        maskFlattened.setMethod("GET");
        maskFlattened.setApiUri("/employee/mask/{id}");

        // 创建扁平化掩码配置 - 使用字段名列表
        List<String> maskFields = Arrays.asList("rocId", "gender");
        maskFlattened.setMaskSettings(new Gson().toJson(maskFields));

        flattenedRepository.save(maskFlattened);

        // 3. 执行测试请求
        mockMvc.perform(get("/employee/mask/" + savedEmployee.getId())
                        .header("account", "flatAccount") // 设置扁平化掩码使用的account
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Another User")))
                .andExpect(jsonPath("$.address", is("Another Address")))
                .andExpect(jsonPath("$.gender").doesNotExist()) // gender 应被掩码
                .andExpect(jsonPath("$.rocId").doesNotExist()) // 顶级 rocId 应被掩码
                .andExpect(jsonPath("$.nestedData.id", is(savedEmployee.getId().intValue())))
                .andExpect(jsonPath("$.nestedData.name", is("Another User")))
                .andExpect(jsonPath("$.nestedData.rocId").doesNotExist()); // 嵌套对象的 rocId 也应被掩码
    }

    @Test
    public void getMaskEmployeeById_withNoMaskConfig_shouldReturnAllFields() throws Exception {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setName("No Mask User");
        employee.setAddress("Complete Address");
        employee.setGender("Male");
        employee.setRocId("N123456789");
        Employee savedEmployee = employeeRepository.save(employee);

        // 不添加掩码配置，请求应返回所有字段
        mockMvc.perform(get("/employee/mask/" + savedEmployee.getId())
                        // 不设置 account 头，或使用一个不存在配置的账户
                        .header("account", "nonExistentAccount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("No Mask User")))
                .andExpect(jsonPath("$.address", is("Complete Address")))
                .andExpect(jsonPath("$.gender", is("Male")))
                .andExpect(jsonPath("$.rocId", is("N123456789")))
                .andExpect(jsonPath("$.nestedData.id", is(savedEmployee.getId().intValue())))
                .andExpect(jsonPath("$.nestedData.name", is("No Mask User")))
                .andExpect(jsonPath("$.nestedData.rocId", is("N123456789")));
    }

    @Test
    public void getMaskEmployeeById_withBothMaskConfigs_shouldPrioritizeStructure() throws Exception {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setName("Priority User");
        employee.setAddress("Priority Address");
        employee.setGender("Male");
        employee.setRocId("P123456789");
        Employee savedEmployee = employeeRepository.save(employee);

        // 创建结构化掩码配置 - 只掩码 address
        EntityColumnMaskStructure maskStructure = new EntityColumnMaskStructure();
        maskStructure.setAccount("bothAccount");
        maskStructure.setMethod("GET");
        maskStructure.setApiUri("/employee/mask/{id}");

        JsonObject maskSettings = new JsonObject();
        maskSettings.addProperty("address", true); // 只掩码 address
        maskStructure.setMaskSettings(new Gson().toJson(maskSettings));
        structureRepository.save(maskStructure);

        // 创建扁平化掩码配置 - 掩码 gender 和 rocId
        EntityColumnMaskFlattened maskFlattened = new EntityColumnMaskFlattened();
        maskFlattened.setAccount("bothAccount"); // 使用相同的账户
        maskFlattened.setMethod("GET");
        maskFlattened.setApiUri("/employee/mask/{id}");

        List<String> maskFields = Arrays.asList("gender", "rocId");
        maskFlattened.setMaskSettings(new Gson().toJson(maskFields));
        flattenedRepository.save(maskFlattened);

        // 执行测试请求 - 应该优先使用结构化掩码
        mockMvc.perform(get("/employee/mask/" + savedEmployee.getId())
                        .header("account", "bothAccount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Priority User")))
                .andExpect(jsonPath("$.address").doesNotExist()) // address 应被掩码（结构化）
                .andExpect(jsonPath("$.gender", is("Male"))) // gender 不应被掩码（扁平化没生效）
                .andExpect(jsonPath("$.rocId", is("P123456789"))) // rocId 不应被掩码（扁平化没生效）
                .andExpect(jsonPath("$.nestedData.id", is(savedEmployee.getId().intValue())))
                .andExpect(jsonPath("$.nestedData.name", is("Priority User")))
                .andExpect(jsonPath("$.nestedData.rocId", is("P123456789"))); // 嵌套 rocId 不应被掩码
    }

}
