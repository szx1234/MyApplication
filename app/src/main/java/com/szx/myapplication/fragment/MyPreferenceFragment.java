package com.szx.myapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import com.szx.myapplication.R;
import com.szx.myapplication.activity.App;
import com.szx.myapplication.util.Const;

/**
 * Created by songzhixin on 2017/6/6.
 */

public class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private SwitchPreference mSp_setting_mode;
    private SwitchPreference mSp_setting_notation;
    private ListPreference mLp_setting_refresh_time;
    private SwitchPreference mSp_setting_tail_state;
    private EditTextPreference mEp_setting_tail_content;
    private Preference mP_setting_application_location;
    private Preference mP_exit;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = getPreferenceManager().getSharedPreferences();
        init();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "setting_application_location":
                break;
            case "setting_exit":
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "setting_mode":
                boolean isNight;
                isNight = sharedPreferences.getBoolean("setting_mode", false);
                if (isNight) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                Toast.makeText(getActivity(), "mode=" + isNight, Toast.LENGTH_SHORT).show();
                break;
            case "setting_notation":
                if (sharedPreferences.getBoolean("setting_notation", false)) {
                    App.openNotation = true;
                } else {
                    App.openNight = false;
                }
                break;
            case "setting_refresh_time":
                String time = sharedPreferences.getString("setting_refresh_time", "5");
                App.refreshTime = Integer.valueOf(time) * Const.SECOND;
                mLp_setting_refresh_time.setSummary(time + "s");
                break;
            case "setting_tail_state":
                if (sharedPreferences.getBoolean("setting_tail_state", false)) {
                    App.openTail = true;
                    mEp_setting_tail_content.setEnabled(true);
                } else {
                    App.openTail = false;
                    mEp_setting_tail_content.setEnabled(false);
                }
                break;
            case "setting_tail_content":
                mEp_setting_tail_content.setSummary(sharedPreferences.getString("setting_tail_content", "自定义你的小尾巴"));
                break;
            default:
                break;
        }
    }

    private void init() {
        //初始化各种Preference
        mSp_setting_mode = (SwitchPreference) findPreference("setting_mode");
        mSp_setting_notation = (SwitchPreference) findPreference("setting_notation");
        mLp_setting_refresh_time = (ListPreference) findPreference("setting_refresh_time");
        mSp_setting_tail_state = (SwitchPreference) findPreference("setting_tail_state");
        mEp_setting_tail_content = (EditTextPreference) findPreference("setting_tail_content");
        mP_setting_application_location = findPreference("setting_application_location");
        mP_exit = findPreference("setting_exit");

        //给Preference设置点击事件
        mP_setting_application_location.setOnPreferenceClickListener(this);
        mP_exit.setOnPreferenceClickListener(this);

        //初始化聊天时间和小尾巴
        String time = sharedPreferences.getString("setting_refresh_time", "建议刷新时间长");
        if (time.length() < 5)
            time = time + "s";
        mLp_setting_refresh_time.setSummary(time);
        mEp_setting_tail_content.setSummary(sharedPreferences.getString("setting_tail_content", "自定义你的小尾巴"));
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

}