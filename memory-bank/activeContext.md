# Active Context (目前開發重點、待解決問題、活躍檔案)

## 當前重點
- 完成了在 `mybatis-flex-core` 專案中建立對應於 `infra/mariadb/init/01-schema.sql` 中定義的所有表格的 entity class 和 mapper 的任務。

## 已處理的最近變更
- 創建了以下 entity class 檔案：
  - `Skill.java`
  - `Team.java`
  - `Vacancy.java`
  - `Resume.java`
  - `AllowedSkillMapping.java`
  - `EntityColumnMaskStructure.java`
  - `EntityColumnMaskFlattened.java`
- 創建了以下 mapper 檔案：
  - `SkillMapper.java`
  - `TeamMapper.java`
  - `VacancyMapper.java`
  - `ResumeMapper.java`
  - `AllowedSkillMappingMapper.java`
  - `EntityColumnMaskStructureMapper.java`
  - `EntityColumnMaskFlattenedMapper.java`
- 更新了以下檔案：
  - `Employee.java`
  - `EmployeeMapper.java`

## 活躍檔案
- `mybatis-flex-core/src/main/java/com/finekuo/mybatisflexcore/entity/*.java`
- `mybatis-flex-core/src/main/java/com/finekuo/mybatisflexcore/mapper/*.java`

## 下一步任務
- 等待用戶進一步指示，確認是否需要對這些檔案進行額外修改或添加新功能。
- 如有需要，可以進行測試以確保這些 entity class 和 mapper 的正確性。

## 需要避免的錯誤
- 確保所有 entity class 使用 Lombok 的 `@Data` 和 `@EqualsAndHashCode` 注解，並正確處理繼承 `BaseEntity`。
- 確保外鍵欄位使用 `Long` 類型，JSON 欄位使用 `String` 類型進行映射。
