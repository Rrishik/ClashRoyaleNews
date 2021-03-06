package com.risk.newsroyale;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.risk.clashroyalenews.R;
import com.risk.newsroyale.adapter.AdapterData;
import com.risk.newsroyale.adapter.NewsAdapter;
import com.risk.newsroyale.helper.Constants;
import com.risk.newsroyale.helper.PageParser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.RecyclerViewClickListener {

    public static RewardedVideoAd mAd;
    private String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private AlertDialog.Builder mErrorMsg;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences mSharedPreferences;
    private String language;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        language = mSharedPreferences.getString(getString(R.string.language_options), "");

        if (language.equals("ku/")) {
            url = "https://cr.kunlun.com/blog/news/";
        } else {
            url = "https://clashroyale.com/" + language + "blog/news/";
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_news);
        mErrorMsg = new AlertDialog.Builder(MainActivity.this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NewsAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                language = mSharedPreferences.getString(getString(R.string.language_options), "");

                if (language.equals("ku/")) {
                    url = "https://cr.kunlun.com/blog/news/";
                } else {
                    url = "https://clashroyale.com/" + language + "blog/news/";
                }
                loadData();
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        loadData();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);

        MobileAds.initialize(MainActivity.this, getString(R.string.ad_app_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAd = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        mAd.loadAd(getString(R.string.video_ad), new AdRequest.Builder().build());
    }

    private void showNews() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorMsg.setMessage("Oops an error occured while fetching the news\nTry again later");
        mErrorMsg.setCancelable(true);
        mErrorMsg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mErrorMsg.show();
    }

    private void loadData() {


        PageParser p = new PageParser(MainActivity.this, url, new PageParser.PageParseListener() {
            @Override
            public void onResponse(List<AdapterData> dataList) {
                mAdapter.setData(dataList);
                mSwipeRefreshLayout.setRefreshing(false);
                showNews();
            }

            @Override
            public void onError(String error) {
                showError();
                mAdapter.clear();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.action_select_language) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(String link) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.Intent.SELECTED_LINK, link);
        startActivity(intent);
    }
}
