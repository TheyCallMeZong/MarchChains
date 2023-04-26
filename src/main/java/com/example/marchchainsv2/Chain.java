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
    //окончания слов
    protected final String[] endings = {
            "ями", "ому", "ая", "ые",  "ий", "ии", "ов", "ие", "ой", "ую", "ых", "ом", "ым", "ого", "ей",
            "ое", "ть",  "у",
    };
    //суффиксы слов
    protected final String[] suffix = {"еньк", "оват", "овит", "енок", "онок", "ива", "ыва", "ева", "онк", "чик", "лив", "щик", "ичк",
            "ышк", "ушк", "ова", "ств", "ниц", "енн", "ущ", "ющ", "ящ", "ющ", "нн", "вш", "ут", "ек",
            "ик", "ив", "ов", "ев", "а", "ш", "о", "е", "я", "и", "е", "у"};

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
        this.exchanger = exchanger;
        this.data = new HashMap<>();
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

    protected abstract String update(String word, String[] pattern);
}
