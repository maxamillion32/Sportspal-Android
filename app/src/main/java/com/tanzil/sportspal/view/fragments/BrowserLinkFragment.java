/**
 * Copyright 2015 Stigasoft.
 */
package com.tanzil.sportspal.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tanzil.sportspal.R;

/**
 * Fragment to display Return and Exchange information.
 */
public class BrowserLinkFragment extends Fragment {

    private static final String TAG = BrowserLinkFragment.class.getName();
    private WebView webView;
    private FragmentActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = new Intent("Header");
        intent.putExtra("message",
                "SP-" + activity.getString(R.string.title_news));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

        View rootView = inflater.inflate(R.layout.fragment_browserlinks,
                container, false);
        // Constants.setHeaderContent(activity,rootView);
        webView = (WebView) rootView.findViewById(R.id.webViewAboutus);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setBuiltInZoomControls(true);

        String linkid = "";
        try {
            if (getArguments() != null) {
                Bundle bndl = getArguments();
                linkid = bndl.getString("link");
//                webView.loadDataWithBaseURL("", linkid, "text/html", "UTF-8",
//                        null);
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        webView.loadUrl(linkid);

        return rootView;
    }

    private class MyBrowser extends WebViewClient {
        ProgressDialog progressDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, request, error);
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        }

        @Override
        public void onReceivedHttpError(WebView view,
                                        WebResourceRequest request, WebResourceResponse errorResponse) {
            // TODO Auto-generated method stub
            super.onReceivedHttpError(view, request, errorResponse);
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("URL", "" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
            // Ignore SSL certificate errors
        }
    }


}
