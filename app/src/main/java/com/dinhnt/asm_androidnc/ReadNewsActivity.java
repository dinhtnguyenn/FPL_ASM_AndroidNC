package com.dinhnt.asm_androidnc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReadNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        WebView webView = findViewById(R.id.webView);

        Intent intent = getIntent();

        String link = intent.getStringExtra("linkNews");

        webView.loadUrl(link);

        webView.setWebViewClient(new WebViewClient());
    }
}