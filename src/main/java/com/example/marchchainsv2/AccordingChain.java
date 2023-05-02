package com.example.marchchainsv2;

import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * Класс для предложения следующего слова
 */
public class AccordingChain extends Chain {
    //Натренированные данные
    private final Map<String, List<String>> data;
    //окончания слов
    private final String[] endings = {
            "ями", "ому", "ая", "ые",  "ий", "ии", "ов", "ие", "ой", "ую", "ых", "ом", "ым", "ого", "ей",
            "ое", "ть",  "у",
    };
    //суффиксы слов
    private final String[] suffix = {"еньк", "оват", "овит", "енок", "онок", "ива", "ыва", "ева", "онк", "чик", "лив", "щик", "ичк",
            "ышк", "ушк", "ова", "ств", "ниц", "енн", "ущ", "ющ", "ящ", "ющ", "нн", "вш", "ут", "ек",
            "ик", "ив", "ов", "ев", "а", "ш", "о", "е", "я", "и", "е", "у"};
    /**
     * Конструктор
     */
    public AccordingChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
        this.data = new HashMap<>();
    }

    /**
     * Имплементация метода тренировки
     */
    @Override
    public void train() {
        Map<String, List<String>> tempResult = new HashMap<>();
        //образуем пары
        for (int i = 0; i < words.size() - 1; i++) {
            String word = update(words.get(i));
            if (tempResult.containsKey(word)) {
                var list = tempResult.get(word);
                list.add(words.get(i + 1));
            } else {
                List<String> arr = new ArrayList<>();
                arr.add(words.get(i + 1));
                tempResult.put(word, arr);
            }
        }

        sort(tempResult);

        for (var e : tempResult.entrySet()) {
            data.put(e.getKey(), e.getValue().stream().distinct().toList());
        }

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
                String[] ms = message.split(" ");
                if (ms.length != 0 && message.length() > 1 && message.charAt(message.length() - 1) == ' ') {
                    String input = update(ms[ms.length - 1]);
                    var res = data.get(input);
                    if (res != null) {
                        System.out.println(res);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод для обновления слова (удаление окончания, суффикса)
     *
     * @param word исходная строка
     * @return обновленную строку
     */
    private String update(String word) {
        String res = update(word, endings);
        return update(res, suffix);
    }

    protected String update(String word, String[] pattern) {
        String tempRes = word;
        for (String end : pattern) {
            if (word.endsWith(end)) {
                tempRes = word.substring(0, word.length() - end.length());
            }
        }
        return tempRes;
    }
}
