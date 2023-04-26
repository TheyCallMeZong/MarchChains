package com.example.marchchainsv2;

import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * Класс для предложения следующего слова
 */
public class AccordingChain extends Chain {
    /**
     * Конструктор
     */
    public AccordingChain(List<String> words, Exchanger<String> exchanger) {
        super(words, exchanger);
    }

    /**
     * Имплементация метода тренировки
     */
    @Override
    public void train() {
        Map<String, List<String>> tempResult = new HashMap<>();
        //образуем пары
        for (int i = 0; i < words.size() - 1; i++){
            String word = update(words.get(i));
            if (tempResult.containsKey(word)){
                var list = tempResult.get(word);
                list.add(words.get(i + 1));
            } else {
                List<String> arr = new ArrayList<>();
                arr.add(words.get(i + 1));
                tempResult.put(word, arr);
            }
        }

        //сортировка по частоте появления слова
        for (Map.Entry<String, List<String>> entry : tempResult.entrySet()) {
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

        for (var e : tempResult.entrySet()){
            data.put(e.getKey(), e.getValue().stream().distinct().toList());
        }

        forecast();
    }

    /**
     * Имплементация слушателя
     */
    @Override
    public synchronized void forecast() {
        while (true){
            try {
                wait();
                message = exchanger.exchange(message);
                String input = update(message);
                var res = data.get(input);
                if (res != null)
                    System.out.println(data.get(input));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String update(String word){
        String res = update(word, endings);
        return update(res, suffix);
    }

    protected String update(String word, String[] pattern){
        String tempRes = word;
        for (String end : pattern){
            if (word.endsWith(end)) {
                tempRes = word.substring(0, word.length() - end.length());
            }
        }
        return tempRes;
    }
}
