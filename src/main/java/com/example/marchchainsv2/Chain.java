package com.example.marchchainsv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 * Абстрактный класс с общими данными для Марковских цепей
 */
public abstract class Chain extends Thread {
    //Натренированные данные
    protected Map<String, List<String>> data;
    //Исходные данные
    protected List<String> words;
    //Текущее слово
    protected String message;
    //Для обмена между потоками
    protected Exchanger<String> exchanger;

    /**
     * Добавлеям сообщение
     */
    public synchronized void setMessage(final String message) {
        this.message = message;
        notifyAll();
    }

    /**
     * Конструктор
     */
    public Chain(List<String> words, Exchanger<String> exchanger) {
        this.words = words;
        data = new HashMap<>();
        this.exchanger = exchanger;
    }

    /**
     * Метод для тренировки нашей модели на исходных данных
     */
    public abstract void train();

    /**
     * Запуск потока
     */
    @Override
    public void run() {
        train();
    }

    /**
     * Слушатель сообщений
     */
    public abstract void forecast();
}
