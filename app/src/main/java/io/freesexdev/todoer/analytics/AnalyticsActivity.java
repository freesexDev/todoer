package io.freesexdev.todoer.analytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yandex.metrica.YandexMetrica;

public class AnalyticsActivity extends AppCompatActivity {

    private static final String API_KEY = "ecac9d66-1f28-47da-a8e5-6b04d89da791";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        YandexMetrica.activate(this, API_KEY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.onPauseActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.onResumeActivity(this);
    }
}
