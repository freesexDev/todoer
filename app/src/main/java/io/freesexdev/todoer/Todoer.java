package io.freesexdev.todoer;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;

public class Todoer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String API_KEY = "ecac9d66-1f28-47da-a8e5-6b04d89da791";
        YandexMetrica.activate(getApplicationContext(), API_KEY);
    }
}
