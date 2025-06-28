# 進度記錄

## 當前狀態
- 完成了在 `mybatis-flex-core` 專案中建立對應於 `infra/mariadb/init/01-schema.sql` 中定義的所有表格的 entity class 和 mapper 的任務。
- 新建立了 Maven 子模組 `by-mybatis-flex`，並設定為 Spring Boot 應用程式，引入了 MyBatis Flex Core 模組，並配置了 MariaDB 連線資訊。
- 以下檔案已創建或更新：
  - **Entity Classes**：
    - `Skill.java`
    - `Team.java`
    - `Vacancy.java`
    - `Resume.java`
    - `AllowedSkillMapping.java`
    - `EntityColumnMaskStructure.java`
    - `EntityColumnMaskFlattened.java`
    - `Employee.java` (已更新)
  - **Mappers**：
    - `SkillMapper.java`
    - `TeamMapper.java`
    - `VacancyMapper.java`
    - `ResumeMapper.java`
    - `AllowedSkillMappingMapper.java`
    - `EntityColumnMaskStructureMapper.java`
    - `EntityColumnMaskFlattenedMapper.java`
    - `EmployeeMapper.java` (已更新)
  - **新模組檔案**：
    - `by-mybatis-flex/pom.xml`
    - `by-mybatis-flex/src/main/java/com/finekuo/bymybatisflex/ByMybatisFlexApplication.java`
    - `by-mybatis-flex/src/main/resources/application.yml`
    - 更新了根目錄的 `pom.xml`，將 `by-mybatis-flex` 加入到 `<modules>` 列表中。

## 已知問題
- 目前無已知問題，所有檔案均按照計劃和用戶回饋進行了設置。

## 下一步計劃
- 等待用戶進一步指示，確認是否需要對 `by-mybatis-flex` 模組進行額外修改或添加新功能。
- 如有需要，可以進行測試以確保新模組的正確性及與 MyBatis Flex Core 的整合。

## 決策演變
- 根據用戶回饋，調整了 entity class 使用 Lombok 的 `@Data` 和 `@EqualsAndHashCode` 注解。
- 確保外鍵欄位使用 `Long` 類型，JSON 欄位使用 `String` 類型進行映射。
- 新模組 `by-mybatis-flex` 的配置確保了與 MariaDB 的連線資訊和 Spring Boot 設定的正確性。
