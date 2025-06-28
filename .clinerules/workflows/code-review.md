---
description: "Cline (AI 助手) 用於審查 Cline 專案 Pull Requests 的詳細工作流程，使用 Git 指令和內部知識。"
author: "Cline Team"
version: "1.0"
tags: ["pr review", "azure devops", "cline project", "ai workflow", "git commands"]
globs: ["*.*"]
---
您可以使用 Git 終端指令。請審查我要求您審查的 PR。您已經在 `cline` 儲存庫中。

<detailed_sequence_of_steps>

# Git PR 審查流程 - 詳細步驟序列

## 1. 收集 PR 資訊

1. 透過取得和切換到 PR 分支來獲取 PR 資訊：

    ```bash
    # 取得最新的變更
    git fetch origin
    
    # 切換到 PR 分支（將 <PR-branch> 替換為實際分支名稱）
    git checkout <PR-branch>
    
    # 取得 PR 分支與主分支之間的差異
    git diff main..HEAD
    ```

2. 取得 PR 的提交資訊：

    ```bash
    # 檢視 PR 中的提交
    git log main..HEAD --oneline
    
    # 取得詳細的提交資訊
    git log main..HEAD --stat
    ```

## 2. 了解上下文

1. 識別 PR 中修改的檔案：

    ```bash
    # 列出所有變更的檔案
    git diff --name-only main..HEAD
    
    # 列出變更的檔案及其狀態
    git diff --name-status main..HEAD
    ```

2. 檢查主分支中的原始檔案以了解上下文：

    ```xml
    <read_file>
    <path>path/to/file</path>
    </read_file>
    ```

3. 對於檔案的特定部分，您可以使用 search_files：
    ```xml
    <search_files>
    <path>path/to/directory</path>
    <regex>search term</regex>
    <file_pattern>*.java</file_pattern>
    </search_files>
    ```

## 3. 分析變更

1. 對於每個修改的檔案，了解：

    - 變更了什麼
    - 為什麼變更（基於 PR 描述）
    - 如何影響程式碼庫
    - 潛在的副作用

2. 尋找：
    - 程式碼品質問題
    - 潛在的錯誤
    - 效能影響
    - 安全疑慮
    - 測試覆蓋率

## 4. 提供審查結果

1. 提供詳細的程式碼審查結果，包含：

    ```xml
    <ask_followup_question>
    <question>基於我對 PR 的審查，我的評估如下：

    [詳細的程式碼審查結果，包含關於 PR 品質、實作和任何發現的問題]

    審查結果：[通過/需要改進/拒絕]

    您希望我提供更詳細的說明嗎？</question>
    <options>["是的，請提供詳細說明", "不，這已經足夠了", "我想討論特定問題"]</options>
    </ask_followup_question>
    ```

## 5. 詢問是否需要評論草稿

1. 詢問是否希望草擬評論供您在 Azure DevOps 上使用：

    ```xml
    <ask_followup_question>
    <question>您希望我為這個 PR 草擬一個評論，讓您可以在 Azure DevOps 網頁介面上使用嗎？</question>
    <options>["是的，請草擬評論", "不，我會自己撰寫評論"]</options>
    </ask_followup_question>
    ```

2. 如果使用者希望草擬評論，提供一個結構良好的評論供複製：

    ```
    感謝您提供這個 PR！以下是我的程式碼審查結果：

    [詳細評估，包含關於 PR 品質、實作和任何建議的重點]

    [包含關於程式碼品質、功能和測試的具體回饋]

    審查結果：[通過/需要改進/拒絕]
    ```

## 6. 總結審查結果

1. 如果程式碼審查通過：

    ```
    ✅ 程式碼審查通過
    - 程式碼品質良好
    - 功能實作正確
    - 測試覆蓋率足夠
    - 沒有發現重大問題
    
    您可以在 Azure DevOps 網頁介面上點擊 "Approve" 來批准這個 PR。
    ```

2. 如果需要改進：

    ```
    ⚠️ 程式碼審查需要改進
    - 發現以下問題：[列出具體問題]
    - 建議的改進：[提供具體建議]
    
    請修正這些問題後重新提交，然後我可以再次審查。
    ```

3. 如果拒絕：

    ```
    ❌ 程式碼審查拒絕
    - 主要問題：[列出主要問題]
    - 需要重大修改才能通過審查
    
    請重新設計並重新提交。
    ```

    注意：審查完成後，您需要回到 Azure DevOps 網頁介面來實際批准或拒絕 PR。
    </detailed_sequence_of_steps>

<example_review_process>

# PR 審查流程範例

讓我們透過一個真實範例來審查修復 Java 模型思考模式計算的 PR。

## 步驟 1：收集 PR 資訊

```bash
# 取得最新變更
git fetch origin

# 切換到 PR 分支
git checkout feature/fix-thinking-mode-calculation

# 取得完整差異
git diff main..HEAD
```

## 步驟 2：了解上下文

```xml
# 檢查原始檔案以了解正在變更的內容
<read_file>
<path>src/main/java/com/example/api/ApiService.java</path>
</read_file>

# 查看 ThinkingBudgetCalculator 元件實作
<read_file>
<path>src/main/java/com/example/component/ThinkingBudgetCalculator.java</path>
</read_file>

# 檢查 API 提供者中目前如何處理思考模式
<search_files>
<path>src/main/java/com/example/api/providers</path>
<regex>reasoningOn</regex>
<file_pattern>*.java</file_pattern>
</search_files>
```

## 步驟 3：分析變更

從 PR 資訊和程式碼檢查中，我們可以看到：

1. PR 修復了 Java 模型思考模式預算計算的問題
2. 目前，思考預算被錯誤地計算為 maxTokens (8192) 的 80%，結果為 6553 個 token
3. Java 模型實際上支援更大的思考預算（最多 64000 個 token）
4. PR 為模型定義添加了 `thinkingConfig` 屬性，`maxBudget: 64000`
5. 它更新 API 處理器以在啟用推理模式時使用此值
6. 它增強了計算器元件以基於模型特定百分比計算最大值
7. 添加了全面的測試來驗證計算

## 步驟 4：提供審查結果

```xml
<ask_followup_question>
<question>基於我對 PR 的審查，我的評估如下：

✅ 程式碼審查通過

1. PR 正確修復了 Java 模型的思考模式預算計算
2. 它正確實作了 Java 模型的 thinkingConfig.maxBudget 屬性 (64000)
3. 它為計算器計算應用了適當的百分比 (50%)
4. 變更經過全面單元測試的良好測試
5. 實作乾淨且遵循專案的編碼標準
6. PR 包含展示修復的有用文件

沒有發現重大問題，程式碼品質良好。

您希望我提供更詳細的說明嗎？</question>
<options>["是的，請提供詳細說明", "不，這已經足夠了", "我想討論特定問題"]</options>
</ask_followup_question>
```

## 步驟 5：詢問是否需要評論草稿

```xml
<ask_followup_question>
<question>您希望我為這個 PR 草擬一個評論，讓您可以在 Azure DevOps 網頁介面上使用嗎？</question>
<options>["是的，請草擬評論", "不，我會自己撰寫評論"]</options>
</ask_followup_question>
```

## 步驟 6：總結審查結果

```
✅ 程式碼審查通過

這個 PR 修復了 Java 模型思考模式預算計算的問題，實作正確且經過良好測試。

您可以在 Azure DevOps 網頁介面上點擊 "Approve" 來批准這個 PR。
```

</example_review_process>

<common_git_commands>

# PR 審查的常用 Git 指令

## 基本 Git 指令

```bash
# 從遠端取得最新變更
git fetch origin

# 檢查目前分支
git branch

# 列出所有分支
git branch -a

# 切換到特定分支
git checkout <branch-name>

# 建立並切換到新分支
git checkout -b <new-branch-name>
```

## 差異和檔案指令

```bash
# 取得分支之間的完整差異
git diff main..<PR-branch>

# 取得特定檔案的差異
git diff main..<PR-branch> -- path/to/file

# 列出分支之間變更的檔案
git diff --name-only main..<PR-branch>

# 列出狀態的檔案（修改、新增、刪除）
git diff --name-status main..<PR-branch>

# 顯示特定檔案的變更
git diff main..<PR-branch> path/to/file
```

## 提交和歷史指令

```bash
# 檢視 PR 中的提交
git log main..<PR-branch> --oneline

# 檢視詳細的提交資訊
git log main..<PR-branch> --stat

# 檢視提交詳細資訊
git show <commit-hash>

# 檢視檔案歷史
git log --follow path/to/file
```

## 其他有用指令

```bash
# 檢查儲存庫狀態
git status

# 檢視遠端儲存庫
git remote -v

# 暫時儲存變更
git stash

# 套用儲存的變更
git stash pop

# 切換回主分支（審查完成後）
git checkout main
```

</common_git_commands>

<general_guidelines_for_commenting>
審查 PR 時，請正常交談，像友善的審查者一樣。您應該保持簡短，並首先感謝 PR 的作者並 @ 提及他們。

無論您是否批准 PR，您都應該對變更進行簡短的總結，不要太冗長或確定性，保持謙遜，就像這是您對變更的理解一樣。就像我現在對您說話的方式一樣。

如果您有任何建議或需要變更的事情，請明確指出問題並提供具體的改進建議。

在程式碼中留下內聯評論是好的，但只有在您對程式碼有具體要說的話時才這樣做。並確保您先留下這些評論，然後提供整體的審查結果。

記住：您的角色是提供程式碼審查結果，實際的批准/拒絕操作需要在 Azure DevOps 網頁介面上進行。
</general_guidelines_for_commenting>

<example_comments_that_i_have_written_before>
<brief_approve_comment>
看起來不錯，不過我們應該在未來讓這對所有提供者和模型都通用
</brief_approve_comment>
<brief_approve_comment>
這對於可能在 OR/Gemini 之間不匹配的模型會有效嗎？比如思考模型？
</brief_approve_comment>
<approve_comment>
這看起來很棒！我喜歡您如何處理全域端點支援 - 將其添加到 ModelInfo 介面完全合理，因為它只是另一個功能標誌，類似於我們處理其他模型功能的方式。

過濾模型列表的方法很乾淨，比硬編碼哪些模型適用於全域端點更容易維護。而且升級 genai 函式庫顯然是讓這項功能運作所必需的。

感謝您也添加了關於限制的文件 - 對使用者來說知道他們不能在全域端點使用上下文快取但可能會減少 429 錯誤是好事。
</approve_comment>
<requesst_changes_comment>
這太棒了。謝謝 @scottsus。

不過我的主要疑慮是 - 這對所有可能的 VS Code 主題都有效嗎？我們最初在這方面遇到了困難，這就是為什麼目前沒有超級樣式化的原因。請測試並分享不同主題的螢幕截圖，確保在我們可以合併之前
</request_changes_comment>
<request_changes_comment>
嘿，PR 整體看起來不錯，但我擔心移除那些超時。那些可能是有原因的 - VSCode 的 UI 在時機方面可能很挑剔。

您能在聚焦側邊欄後重新添加超時嗎？像這樣：

```java
// 在 Java 中，您可能會使用 Thread.sleep 或 CompletableFuture.delayedExecutor
CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS)
    .execute(() -> {
        // 聚焦側邊欄邏輯在這裡
        visibleWebview = WebviewProvider.getSidebarInstance();
    });
```

</request_changes_comment>
<request_changes_comment>
嘿 @alejandropta 謝謝您處理這個！

幾個注意事項：
1 - 向環境變數添加額外資訊相當有問題，因為環境變數會附加到**每條訊息**。我不認為對於一個相對小眾的使用案例來說這是合理的。
2 - 將此選項添加到設定中可能是一個選項，但我們希望我們的選項對新使用者來說簡單明瞭
3 - 我們正在重新視覺化設定頁面的顯示/組織方式，一旦完成並且我們的設定頁面更清楚地劃分，這可能會得到調解。

所以在設定頁面更新之前，並且以乾淨且不會讓新使用者困惑的方式添加到設定中，我認為我們無法合併這個。請耐心等待我們。
</request_changes_comment>
<request_changes_comment>
另外，別忘了添加 changeset，因為這修復了一個面向使用者的錯誤。

架構變更很紮實 - 將焦點邏輯移到命令處理器是有意義的。只是不想通過移除那些超時來引入微妙的時機問題。
</request_changes_comment>
</example_comments_that_i_have_written_before>