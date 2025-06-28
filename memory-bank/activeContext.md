# Active Context (目前開發重點、待解決問題、活躍檔案)

## 當前重點
- 完成了在 `mybatis-flex-core` 專案中建立對應於 `infra/mariadb/init/01-schema.sql` 中定義的所有表格的 entity class 和 mapper 的任務。
- 新建立了 Maven 子模組 `by-mybatis-flex`，並設定為 Spring Boot 應用程式，引入了 MyBatis Flex Core 模組，並配置了 MariaDB 連線資訊。

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
- 新增了以下檔案：
  - `by-mybatis-flex/pom.xml`
  - `by-mybatis-flex/src/main/java/com/finekuo/bymybatisflex/ByMybatisFlexApplication.java`
  - `by-mybatis-flex/src/main/resources/application.yml`
- 更新了根目錄的 `pom.xml`，將 `by-mybatis-flex` 加入到 `<modules>` 列表中。

## 活躍檔案
- `mybatis-flex-core/src/main/java/com/finekuo/mybatisflexcore/entity/*.java`
- `mybatis-flex-core/src/main/java/com/finekuo/mybatisflexcore/mapper/*.java`
- `by-mybatis-flex/pom.xml`
- `by-mybatis-flex/src/main/java/com/finekuo/bymybatisflex/ByMybatisFlexApplication.java`
- `by-mybatis-flex/src/main/resources/application.yml`

## 下一步任務
- 等待用戶進一步指示，確認是否需要對 `by-mybatis-flex` 模組進行額外修改或添加新功能。
- 如有需要，可以進行測試以確保新模組的正確性及與 MyBatis Flex Core 的整合。

## 需要避免的錯誤
- 確保所有 entity class 使用 Lombok 的 `@Data` 和 `@EqualsAndHashCode` 注解，並正確處理繼承 `BaseEntity`。
- 確保外鍵欄位使用 `Long` 類型，JSON 欄位使用 `String` 類型進行映射。
- 確保新模組 `by-mybatis-flex` 的配置正確，特別是 MariaDB 連線資訊和 Spring Boot 設定。
