package io.freesexdev.todoer;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;

public class Todoer extends Application {

    public String API_KEY = "ecac9d66-1f28-47da-a8e5-6b04d89da791";

    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetrica.activate(getApplicationContext(), API_KEY);
    }
}
