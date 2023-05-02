package com.example.marchchainsv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 * Абстрактный класс с общими данными для Марковских цепей
 */
public abstract class Chain extends Thread {
    //Исходные данные
    protected final List<String> words;

    //Для обмена между потоками
    protected final Exchanger<String> exchanger;

    //Текущее слово
    protected String message;

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

    /**
     * сортировка по частоте появления
     * @param data входные данные
     */
    protected void sort(Map<String, List<String>> data){
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            List<String> sheet = entry.getValue();
            HashMap<String, Integer> wordFreqMap = new HashMap<>();

            for (String word : sheet) {
                if (wordFreqMap.containsKey(word)) {
                    wordFreqMap.put(word, wordFreqMap.get(word) + 1);
                } else {
                    wordFreqMap.put(word, 1);
                }
            }

            sheet.sort((word1, word2) -> {
                int freq1 = wordFreqMap.get(word1);
                int freq2 = wordFreqMap.get(word2);
                return Integer.compare(freq2, freq1);
            });
        }
    }
}
