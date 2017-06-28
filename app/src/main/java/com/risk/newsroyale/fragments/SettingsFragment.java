package com.risk.newsroyale.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.risk.clashroyalenews.R;

import static com.risk.newsroyale.MainActivity.mAd;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        Preference ad = findPreference(getString(R.string.video_ad));
        ad.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mAd.isLoaded()) {
                    mAd.show();
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        mAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAd.destroy(getActivity());
        super.onDestroy();
    }
}
