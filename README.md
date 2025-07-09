# WFID Verify Android 專案

這是一個 Android 應用程式，使用 WebView 來載入網頁並處理 `doWFIDVerify` 函數的呼叫。

## 功能特色

- **WebView 整合**：使用 Android WebView 載入 HTML 網頁
- **JavaScript 介面**：透過 `@JavascriptInterface` 處理網頁的 JavaScript 呼叫
- **JSON 資料處理**：解析和處理來自網頁的 JSON 資料
- **非同步處理**：在背景執行緒處理驗證邏輯
- **使用者回饋**：透過 Toast 訊息顯示處理狀態

## 專案結構

```
WFIDVerifyAndroid/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/wfidverify/
│   │   │   └── MainActivity.java          # 主要活動類別
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml     # 佈局檔案
│   │   │   ├── values/
│   │   │   │   ├── strings.xml           # 字串資源
│   │   │   │   ├── colors.xml            # 顏色資源
│   │   │   │   └── themes.xml            # 主題資源
│   │   │   └── ...
│   │   ├── assets/
│   │   │   └── wfid-verify-demo.html     # HTML 網頁檔案
│   │   └── AndroidManifest.xml           # 應用程式清單
│   └── build.gradle                      # 應用程式級別建置檔案
├── build.gradle                          # 專案級別建置檔案
└── settings.gradle                       # 設定檔案
```

## 建置和執行

### 前置需求

1. **Android Studio**：最新版本的 Android Studio
2. **Android SDK**：API Level 21 或更高版本
3. **Java Development Kit (JDK)**：版本 8 或更高

### 建置步驟

1. **開啟專案**
   ```bash
   # 在 Android Studio 中開啟 WFIDVerifyAndroid 資料夾
   ```

2. **同步 Gradle**
   - 在 Android Studio 中點擊 "Sync Project with Gradle Files"
   - 或執行 `./gradlew build`

3. **建置 APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **安裝到裝置**
   ```bash
   ./gradlew installDebug
   ```

### 執行應用程式

1. 在 Android 裝置或模擬器上啟動應用程式
2. 應用程式會自動載入 `wfid-verify-demo.html` 網頁
3. 在網頁中填寫驗證資料並點擊「執行驗證」按鈕
4. 觀察 Android 應用程式的 Toast 訊息和 Logcat 輸出

## 核心功能說明

### MainActivity.java

主要功能包括：

- **WebView 設定**：啟用 JavaScript、DOM storage 等
- **JavaScript 介面**：透過 `WFIDVerifyInterface` 類別處理網頁呼叫
- **資料處理**：解析 JSON 資料並進行驗證處理
- **非同步處理**：在背景執行緒處理驗證邏輯

### JavaScript 介面

```java
@JavascriptInterface
public void WFIDVerify(String jsonData) {
    // 處理來自網頁的驗證請求
    // 解析 JSON 資料
    // 執行驗證邏輯
    // 回傳結果給網頁
}
```

### 資料流程

1. **網頁端**：使用者填寫表單並點擊驗證按鈕
2. **JavaScript**：呼叫 `doWFIDVerify()` 函數
3. **Android 端**：`WFIDVerifyInterface.WFIDVerify()` 接收資料
4. **處理邏輯**：解析 JSON 並執行驗證
5. **回傳結果**：透過 `evaluateJavascript()` 回傳結果給網頁

## 自訂和擴展

### 修改驗證邏輯

在 `processVerification()` 方法中實作您的驗證邏輯：

```java
private void processVerification(JSONObject verificationData) {
    // 實作您的驗證邏輯
    // 例如：呼叫 API、驗證憑證、檢查資料庫等
}
```

### 載入不同的網頁

修改 `loadWebPage()` 方法：

```java
private void loadWebPage() {
    // 載入本地檔案
    webView.loadUrl("file:///android_asset/your-html-file.html");
    
    // 或載入網路檔案
    webView.loadUrl("https://your-domain.com/your-html-file.html");
}
```

### 添加更多 JavaScript 介面

在 `WFIDVerifyInterface` 類別中添加更多方法：

```java
@JavascriptInterface
public void anotherFunction(String data) {
    // 處理其他 JavaScript 呼叫
}
```

## 除錯和測試

### Logcat 輸出

應用程式會在 Logcat 中輸出詳細的除錯資訊：

```
D/WFIDVerify: 收到 WFIDVerify 呼叫: {"userId":"user123",...}
D/WFIDVerify: 處理驗證資料: {...}
```

### 測試步驟

1. 啟動應用程式
2. 確認網頁正確載入
3. 填寫表單資料
4. 點擊驗證按鈕
5. 檢查 Toast 訊息和 Logcat 輸出
6. 確認網頁收到回應

## 注意事項

- 確保 Android 裝置有網路連線（如果載入網路檔案）
- 檢查 Android 裝置的 JavaScript 設定
- 在真機上測試以確保完整功能
- 注意 WebView 的安全性設定

## 授權

此專案僅供學習和測試使用。 