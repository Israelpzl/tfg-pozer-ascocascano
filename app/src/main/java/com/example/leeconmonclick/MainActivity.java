package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCenter.start(getApplication(), "3e7f65ac-796a-431c-94f1-ce9ba68cab98", Analytics.class, Crashes.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}