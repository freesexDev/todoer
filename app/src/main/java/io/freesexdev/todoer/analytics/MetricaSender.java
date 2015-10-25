package io.freesexdev.todoer.analytics;

import com.yandex.metrica.YandexMetrica;

public class MetricaSender {
    public MetricaSender sendTask(String message) {
        MetricaSender sender = new MetricaSender();
        YandexMetrica.reportEvent("Добавлено напоминание" + message);
        return sender;
    }
}
