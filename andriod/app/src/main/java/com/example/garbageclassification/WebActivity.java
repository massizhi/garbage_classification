package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //加载搜索结果界面
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String spSearch = sharedPreferences.getString("search", "");//得到搜索词

        webView = (WebView)findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://baike.baidu.com/item/"+spSearch);
    }
}