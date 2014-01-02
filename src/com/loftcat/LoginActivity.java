/*
 * Copyright (c) 2013 HeBin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loftcat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 登录百度账号初始化Channel的Activity
 */
public class LoginActivity extends Activity {

    /** log tag. */
    private static final String TAG = LoginActivity.class.getSimpleName();

    private WebView mWebView;

    /** redirect uri 值为"oob" */
    private static final String REDIRECT = "oob";
    
    /** 开发中心 */
    static final String DEV_CENTER = "https://openapi.baidu.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(LoginActivity.this);

        setContentView(mWebView);

        initWebView(mWebView);

        getAccessToken();
    }

    /**
     * 获取 access token的url
     */
    private void getAccessToken() {
        String url = DEV_CENTER + "oauth/2.0/authorize?" 
                                + "response_type=token" 
                                + "&client_id=" + Utils.getMetaValue(LoginActivity.this, "api_key")
                                + "&redirect_uri=" + REDIRECT 
                                + "&display=mobile";

        Log.d(TAG, "GetAccessTokenUrl: " + url);

        mWebView.loadUrl(url);
    }

    /**
     * 设置Webview的WebviewClient
     * @param webview webview
     */
	private void initWebView(WebView webview) {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); 
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.startsWith(REDIRECT) || url.contains("login_success")) {

                    // change # -> ? 
                    int fragmentIndex = url.indexOf("#");
                    url = "http://localhost/?" + url.substring(fragmentIndex + 1);

                    // 从URL中获得Access token
                    String accessToken = Uri.parse(url).getQueryParameter("access_token");
                    Log.d(TAG, ">>> Get Original AccessToken: \r\n" + accessToken);

                    
                    Toast.makeText(LoginActivity.this, "get access token success", Toast.LENGTH_SHORT).show();

                    // ͨ登录
                    Intent intent = new Intent(Utils.ACTION_LOGIN);
                    intent.setClass(LoginActivity.this, PushDemoActivity.class);
                    intent.putExtra(Utils.EXTRA_ACCESS_TOKEN, accessToken);
                    startActivity(intent);

                    finish();
                }
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (goBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean goBack() {
        WebView webView = mWebView;
        if (webView != null && webView.canGoBack()) {
            webView.goBack();

            return true;
        }

        return false;
    }
}
