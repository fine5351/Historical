# 進度記錄

## 當前狀態
- 完成了在 `mybatis-flex-core` 專案中建立對應於 `infra/mariadb/init/01-schema.sql` 中定義的所有表格的 entity class 和 mapper 的任務。
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

## 已知問題
- 目前無已知問題，所有檔案均按照計劃和用戶回饋進行了設置。

## 下一步計劃
- 等待用戶進一步指示，確認是否需要對這些檔案進行額外修改或添加新功能。
- 如有需要，可以進行測試以確保這些 entity class 和 mapper 的正確性。

## 決策演變
- 根據用戶回饋，調整了 entity class 使用 Lombok 的 `@Data` 和 `@EqualsAndHashCode` 注解。
- 確保外鍵欄位使用 `Long` 類型，JSON 欄位使用 `String` 類型進行映射。
