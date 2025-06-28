# Active Context (目前開發重點、待解決問題、活躍檔案)

## 當前重點

- 實作 HR 系統的基本 CRUD 操作
- 設定 Spring Boot 專案框架與 MyBatis-Flex 資料庫連線

## 已處理的最近變更

- 新建 module by-mybatis-flex，繼承於 root 成為子模組，引用 mybatis-flex-core
- 初始化 Spring Boot 專案結構與 Maven 設定
- 創建 PlantUML 圖表 (sequence 和 activity diagram) 存放在 docs/feature/by-mybatis-flex

## 活躍檔案

- `by-mybatis-flex/pom.xml`
- `by-mybatis-flex/src/main/java/com/finekuo/hr/HrApplication.java`
- `by-mybatis-flex/src/main/resources/application.properties`
- `by-mybatis-flex/src/test/resources/application-test.properties`
- `docs/feature/by-mybatis-flex/hr-crud-sequence.puml`
- `docs/feature/by-mybatis-flex/hr-crud-activity.puml`

## 下一步任務

- 實作 HR 系統的基本 CRUD 操作類別和對應的 Java 版 Mapper
- 建立單元測試，確保服務層覆蓋率 > 80%
- 建立整合測試，使用 H2 Database 模擬資料庫環境

## 需要避免的錯誤
