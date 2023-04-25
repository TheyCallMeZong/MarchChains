package com.example.marchchainsv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Chain extends Thread {
    protected Map<String, List<String>> data;
    protected List<String> words;
    protected String message;
    protected Exchanger<String> exchanger;

    public synchronized void setMessage(final String message) {
        this.message = message;
        notifyAll();
    }

    public Chain(List<String> words, Exchanger<String> exchanger) {
        this.words = words;
        data = new HashMap<>();
        this.exchanger = exchanger;
    }

    public abstract void train();

    @Override
    public void run() {
        train();
    }

    public abstract void forecast();
}
