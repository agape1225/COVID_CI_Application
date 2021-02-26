package com.cookandroid.k_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

public class Bframe extends Selfdiagnosis{
    private WebView mWebView2;
    private Button button2;

    // @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bframe, container, false);
        mWebView2 = (WebView) view.findViewById(R.id.webView2);
        button2 = (Button) view.findViewById(R.id.back);

        mWebView2.setWebViewClient(new WebViewClient());
        mWebView2.setNetworkAvailable(true);
        mWebView2.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView2.setWebChromeClient(new WebChromeClient());
        mWebView2.getSettings().setJavaScriptEnabled(true);
        mWebView2.loadUrl("file:///android_asset/www/WeloAdult.html");

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Selfdiagnosis frame = new Selfdiagnosis();
                transaction.replace(R.id.container,frame);
                transaction.commit();
            }
        });
        return view;
    }
}