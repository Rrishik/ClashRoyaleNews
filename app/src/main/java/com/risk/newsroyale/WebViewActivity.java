package com.risk.newsroyale;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.risk.clashroyalenews.R;
import com.risk.newsroyale.helper.Constants;
import com.risk.newsroyale.helper.Util;

public class WebViewActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private WebView mWebPage;
    private String mLink;
    private ProgressBar mProgressBar;
    private Util mWebViewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_page);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Clash Royale");
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mWebPage = (WebView) findViewById(R.id.wv_page);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(TAG, "No data specified for webview");
            return;
        }
        mLink = extras.getString(Constants.Intent.SELECTED_LINK);
        if (mLink == null || mLink.trim().isEmpty()) {
            Log.e(TAG, "No URL specified for webview");
            return;
        }

        MobileAds.initialize(WebViewActivity.this, getString(R.string.ad_app_id));
        AdView mAdView = (AdView) findViewById(R.id.adWebView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mWebViewUtil = new Util(WebViewActivity.this, mLink, mWebPage, mProgressBar);
        mWebViewUtil.openInWebView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.open_in_browser:
                mWebViewUtil.openInBrowser();
                return true;
            case R.id.copy_link:
                mWebViewUtil.copyLink();
                return true;
            case R.id.share_link:
                mWebViewUtil.shareLink();
                return true;
            case R.id.page_refresh:
                mWebViewUtil.refresh();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebPage.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebPage.onResume();
    }
}
