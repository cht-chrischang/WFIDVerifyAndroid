package com.example.wfidverify;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "WFIDVerify";
    private WebView webView;
    private EditText urlInput;
    private Button loadButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        urlInput = findViewById(R.id.urlInput);
        loadButton = findViewById(R.id.loadButton);
        setupWebView();
        setupUrlLoader();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        
        // 啟用 JavaScript
        webSettings.setJavaScriptEnabled(true);
        
        // 啟用 DOM storage
        webSettings.setDomStorageEnabled(true);
        
        // 啟用混合內容 (HTTP + HTTPS)
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        // 啟用檔案存取
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // 設定 User Agent
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " WFIDVerifyApp/1.0");

        // 設定 WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "網頁載入完成: " + url);
                Toast.makeText(MainActivity.this, "網頁已載入完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                String msg = "網頁載入錯誤: " + error.getDescription();
                Log.e(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 僅限開發測試用：忽略所有 SSL 憑證錯誤
                Toast.makeText(MainActivity.this, "警告：忽略 SSL 憑證錯誤，僅供開發測試用", Toast.LENGTH_LONG).show();
                handler.proceed();
            }
        });

        // 添加 JavaScript 介面
        webView.addJavascriptInterface(new WFIDVerifyInterface(), "androidmobile");
    }

    private void setupUrlLoader() {
        loadButton.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            if (!url.isEmpty()) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                webView.loadUrl(url);
            } else {
                Toast.makeText(this, "請輸入網址", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // JavaScript 介面類別
    public class WFIDVerifyInterface {
        
        @JavascriptInterface
        public void WFIDVerify(String jsonData) {
            Log.d(TAG, "收到 WFIDVerify 呼叫: " + jsonData);
            
            try {
                // 解析 JSON 資料
                JSONObject jsonObject = new JSONObject(jsonData);
                
                // 在 UI 執行緒中顯示 Toast
                runOnUiThread(() -> {
                    String message = "收到驗證請求:\n";
                    message += "用戶ID: " + jsonObject.optString("userId", "未知") + "\n";
                    message += "會話ID: " + jsonObject.optString("sessionId", "未知") + "\n";
                    message += "平台: " + jsonObject.optString("platform", "未知");
                    
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                });
                
                // 模擬處理驗證邏輯
                processVerification(jsonObject);
                
            } catch (JSONException e) {
                Log.e(TAG, "JSON 解析錯誤", e);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "資料格式錯誤", Toast.LENGTH_SHORT).show();
                });
            }
        }
        
        private void processVerification(JSONObject verificationData) {
            // 這裡可以實作實際的驗證邏輯
            // 例如：呼叫 API、驗證憑證、檢查資料庫等
            
            Log.d(TAG, "處理驗證資料: " + verificationData.toString());
            
            // 模擬驗證處理時間
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // 模擬 2 秒處理時間
                    
                    // 模擬驗證結果
                    JSONObject result = new JSONObject();
                    result.put("success", true);
                    result.put("message", "驗證成功");
                    result.put("timestamp", System.currentTimeMillis());
                    result.put("originalData", verificationData.toString());
                    
                    // 將結果回傳給網頁
                    final String jsCode = "javascript:simulateMobileResponse(" + result.toString() + ")";
                    
                    runOnUiThread(() -> {
                        webView.evaluateJavascript(jsCode, null);
                        Toast.makeText(MainActivity.this, "驗證處理完成", Toast.LENGTH_SHORT).show();
                    });
                    
                } catch (Exception e) {
                    Log.e(TAG, "驗證處理錯誤", e);
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "驗證處理失敗", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
} 