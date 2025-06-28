# Development Workflow (開發與測試流程)

## 開發流程

1. 使用 git 指令建立 feature 分支
2. 使用 plantuml 規劃 sequence、activity、class、state...等相關 diagram 放於 docs/{branchName} 下
3. 依照uml規劃完成功能
4. 每完成一個需要修改的 class 就使用 git 指令做 commit
5. 建立 unit test
6. 運行 mvn clean compile test 進行測試, 並修正到通過
7. 建立 @SpringBootTest
8. 運行 mvn clean compile test 進行測試, 並修正到通過
9. 使用 git 指令 push