package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lenovo.cbs.R;

public class SiteFragment extends Fragment {
    public static SiteFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url",url);
        SiteFragment fragment = new SiteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site,container,false);
        WebView webView = view.findViewById(R.id.web);
        Bundle bundle = getArguments();
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        if (bundle!=null) {
            webView.loadUrl(bundle.getString("url"));
        }
        return view;
    }
}
