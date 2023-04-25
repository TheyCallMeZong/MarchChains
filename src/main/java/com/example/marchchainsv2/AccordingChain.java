package com.example.marchchainsv2;


import java.util.List;
import java.util.concurrent.Exchanger;

//Вводи слово - предложение следующего слова
public class AccordingChain extends Chain {
    public AccordingChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
    }

    @Override
    public void train() {
        forecast();
    }

    @Override
    public synchronized void forecast() {
        while (true){
            try {
                wait();
                message = exchanger.exchange(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
