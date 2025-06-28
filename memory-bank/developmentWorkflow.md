# Development Workflow (開發與測試流程)

## 開發流程  
1. 從 `activeContext.md` 讀取任務  
2. 使用 git 指令建立 feature 分支  
3. 使用 plantuml 規劃 sequence、activity diagram 放於 docs/{branchName} 下
4. 依照規劃完成功能開發且每完成一個 class 需要修改的範圍就使用 git 指令 commit
5. 完成開發後更新 `activeContext.md`  
6. 建立 unit test
7. 運行 mvn clean compile test 進行測試, 並修正到通過
8. 建立 @SpringBootTest
9. 運行 mvn clean compile test 進行測試, 並修正到通過
10. 更新 `activeContext.md` 
11. 使用 git 指令 push

## 測試準則  
- 所有服務層需有單元測試覆蓋率 > 80%（使用 JUnit 5）  
- 整合測試需使用 H2 Database 模擬資料庫環境，確保 CRUD 操作正確性  
- 使用 mock 資料測試業務邏輯和 API 端點
