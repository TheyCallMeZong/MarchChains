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

/**
 * Контроллер главной формы
 */
public class MainController {
    //путь до данных для обучения
    private static final String fileName = "test.txt";
    private static final List<Chain> chains = new ArrayList<>();
    //Поле для ввода
    @FXML
    public TextField inputBox;
    //Кнопка, куда вместо текста будет записываться предложенное слово
    @FXML
    private Button recommendation1;
    //Кнопка, куда вместо текста будет записываться предложенное слово
    @FXML
    private Button recommendation2;
    //Кнопка, куда вместо текста будет записываться предложенное слово
    @FXML
    private Button recommendation3;

    @FXML
    void initialize(){
        inputBox.textProperty().addListener((observable, oldValue, newValue) -> {
            for (Chain chain : chains){
                chain.setMessage(newValue);
            }
        });
    }

    public MainController() throws IOException {
        //получаем содержимое файла, удаляя все пробелы и "—"
        var content = Arrays.stream(Files.lines(Paths.get(fileName)).reduce("", String::concat)
                        .split(" "))
                        .filter(x -> !x.isEmpty() && !x.equals("—"))
                        .map(x -> x.replace(";", ""))
                        .map(x -> x.replace(",", ""))
                        .map(x -> x.replace(".", ""))
                        .map(x -> x.replace("!", ""))
                        .map(x -> x.replace("…", ""))
                        .map(x -> x.replace(":", ""))
                        .map(x -> x.replace("?", ""))
                        .map(x -> x.replace("«", ""))
                        .map(x -> x.replace("»", ""))
                        .map(x -> x.replace("(", ""))
                        .map(x -> x.replace(")", ""))
                        .map(x -> x.replace("'", ""))
                        .map(x -> x.replace("`", ""))
                        .map(x -> x.replace("[", ""))
                        .map(x -> x.replace("]", ""))
                        .map(x -> x.replace("}", ""))
                        .map(x -> x.replace("{", ""))
                        .map(x -> x.replace("{", ""))
                        .map(x -> x.replace("~", ""))
                        .map(x -> x.replace("±", ""))
                        .map(x -> x.replace("<", ""))
                        .map(x -> x.replace(">", ""))
                        .map(x -> x.replace("_", ""))
                        .map(x -> x.replace("-", ""))
                        .map(x -> x.replace("/", ""))
                        .map(x -> x.replace("\"", ""))
                        .map(x -> x.replace("\\", ""))
                        .map(x -> x.replace("@", ""))
                        .map(x -> x.replace("#", ""))
                        .map(x -> x.replace("$", ""))
                        .map(x -> x.replace("%", ""))
                        .map(x -> x.replace("^", ""))
                        .map(x -> x.replace("&", ""))
                        .map(x -> x.replace("*", ""))
                        .map(x -> x.replace("=", ""))
                        .map(String::toLowerCase)
                        .toList();
        //запускаем потоки для "обучения" на нашем тексте
        //класс для обмена данными между потоками
        Exchanger<String> exchanger = new Exchanger<>();
        chains.add(new AccordingChain(content, exchanger));
        chains.add(new SpellChain(content, exchanger));

        for (Chain chain : chains) {
            chain.start();
        }
    }
}
