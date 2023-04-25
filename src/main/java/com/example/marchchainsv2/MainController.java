package com.example.marchchainsv2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;

public class MainController {
    //путь до данных для обучения
    private static final String fileName = "train.txt";
    //Марковская цепь для предсказания слов
    private final AccordingChain accordingChain;
    //Марковская цепь для предсказания конца слов
    private final SpellChain spellChain;
    @FXML
    public TextField inputBox;
    @FXML
    private Button recommendation1;
    @FXML
    private Button recommendation2;
    @FXML
    private Button recommendation3;

    @FXML
    void initialize(){
        inputBox.textProperty().addListener((observable, oldValue, newValue) -> {
            accordingChain.setMessage(newValue);
            spellChain.setMessage(newValue);
        });
    }

    public MainController() throws IOException, InterruptedException {
        //содержимое файла
        var content = Arrays.stream(Files.lines(Paths.get(fileName)).reduce("", String::concat)
                        .split(" ")).filter(x -> !x.isEmpty() && !x.equals("—")).toList();

        //запускаем потоки для "обучения" на нашем тексте
        //класс для обмена данными между потоками
        Exchanger<String> exchanger = new Exchanger<>();
        spellChain = new SpellChain(content, exchanger);
        accordingChain = new AccordingChain(content, exchanger);
        spellChain.start();
        accordingChain.start();
    }
}
