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

public class Aframe extends Selfdiagnosis {
    private WebView mWebView;
    private Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aframe, container, false);
        button = (Button) view.findViewById(R.id.back0);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.loadUrl("https://hcs.eduro.go.kr/#/loginHome");
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient());

        button.setOnClickListener(new Button.OnClickListener() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

}