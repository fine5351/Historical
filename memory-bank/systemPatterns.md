# System Patterns (技術架構與設計規則)

## 技術架構  
- 後端：Spring Boot 3.4+ + MyBatis-Flex  
- 資料存取：MariaDB 或 Oracle DB  
- 消息佇列：NATS（若需要非同步處理或事件驅動架構）  

## Database schema 設計模式  
- 資料表以唯一識別碼（ID）作為主索引  
- 使用 Spring Boot 的 MVC 架構進行層次化設計（Controller、Service、Mapper）  
- MyBatis-Flex 使用規範：
  - 不使用 XML 檔案配置 SQL，一律使用 Java 程式碼在 `Mapper.java` 中定義 SQL  
  - `WHERE` 條件數量少於 4 個時，優先使用 `BaseMapper` 的 `QueryWrapper`  
  - `WHERE` 條件數量超過 4 個時，使用 `@Select`、`@Update` 等註解撰寫原生 SQL  
  - SQL 語句優先使用文字區塊（Text Block）格式  
  - 不需要 `<script></script>` 標籤時不添加  

## 系統流程模式  
- 前端或其他系統透過 RESTful API 請求後端服務 → Controller 處理請求 → Service 執行業務邏輯 → Mapper 進行資料庫操作  
- 所有資料庫操作集中處理於 `Mapper.java` 檔案  

## 事件處理模式  
- 

## http 回應格式
  - HTTP Status 200 + error code + error message
    - 參考 normal-core\src\main\java\com\finekuo\normalcore\dto\response\BaseResponse.java
