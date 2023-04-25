package com.example.marchchainsv2;

import java.util.List;
import java.util.concurrent.Exchanger;

//Когда вводим одну букву, нам дается подсказка на ввод необходимого слова
public class SpellChain extends Chain{
    public SpellChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
    }

    @Override
    public void train() {
        forecast();
    }

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
}
