package com.example.marchchainsv2;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Класс для подсказки слова по буквам
 */
public class SpellChain extends Chain{
    /**
     * Конструктор
     */
    public SpellChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
    }

    /**
     * Имплементация метода тренировки
     */
    @Override
    public void train() {
        forecast();
    }

    /**
     * Имплементация слушателя
     */
    @Override
    public synchronized void forecast() {
        while (true) {
            try {
                wait();
                message = exchanger.exchange(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected String update(String word, String[] pattern) {
        return null;
    }
}
