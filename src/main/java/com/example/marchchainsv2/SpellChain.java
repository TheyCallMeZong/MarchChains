package com.example.marchchainsv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 * Класс для подсказки слова по буквам
 */
public class SpellChain extends Chain {
    private final HashMap<Integer, HashMap<String, List<String>>> data;
    /**
     * Конструктор
     */
    public SpellChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
        this.data = new HashMap<>();
    }

    /**
     * Имплементация метода тренировки
     */
    @Override
    public void train() {
        var tempRes = new HashMap<Integer, HashMap<String, List<String>>>();
        for (int i = 0; i < 5; i++){
            tempRes.put(i, new HashMap<>());
        }
        for (String word : words) {
            for (int j = 0; j < word.length() && j < 5; j++) {
                String tempWord = word.substring(0, j + 1);
                if (tempRes.get(j).containsKey(tempWord)){
                    var tempList = tempRes.get(j).get(tempWord);
                    tempList.add(word);
                } else {
                    List<String> tempArr = new ArrayList<>();
                    tempArr.add(word);
                    tempRes.get(j).put(tempWord, tempArr);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            sort(tempRes.get(i));
        }

        for (int i = 0; i < tempRes.size(); i++){
            HashMap<String, List<String>> tempMap = new HashMap<>();
            for (var e : tempRes.get(i).entrySet()){
                List<String> uniq = new ArrayList<>(e.getValue().stream().distinct().toList());
                tempMap.put(e.getKey(), uniq);
            }
            data.put(i, tempMap);
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
                String[] input = message.split(" ");
                if (message.length() > 0 && message.charAt(message.length() - 1) != ' ' && input[input.length - 1].length() < 5){
                    var result = data.get(input[input.length - 1].length() - 1).get(input[input.length - 1]);
                    System.out.println(result);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
