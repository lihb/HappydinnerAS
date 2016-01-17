package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;


public class WebViewActivity extends BaseActivity {

//    private ImageView mBackImg = null;
//    private TextView mTitleTextView = null;
    private ProgressBar mProgressBar = null;
    private WebView mWebView = null;
    private String mUrl = "about:blank";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if(mWebView != null) {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if(mWebView != null) {
            mWebView.loadUrl("about:blank");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initViews();
    }

    private void initViews() {
//        mBackImg = (ImageView) findViewById(R.id.imageView_back);
//        mBackImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

//        mTitleTextView = (TextView) findViewById(R.id.textView_title);
//        mTitleTextView.setText(getIntent().getStringExtra("title"));

        mProgressBar = (ProgressBar) findViewById(R.id.showAdvProgressBar);
        mProgressBar.setProgress(0);

        mWebView = (WebView) findViewById(R.id.showAdvWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }

//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                if (TextUtils.isEmpty(getIntent().getStringExtra("title"))) { // 如果没给标题，就从网页中获取标题
//                    mTitleTextView.setText(title);
//                }
//            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mUrl = getIntent().getStringExtra("url");
    }


    @Override
    public void finish() {
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        if (viewGroup != null) {
            // Bug: Activity has leaked window android.widget.ZoomButtonsController that was originally added here android.view.WindowLeaked
            // 移除所有控件，防止关闭该activity时，内存泄漏的问题
            viewGroup.removeAllViews();
        }
        super.finish();
    }
}
