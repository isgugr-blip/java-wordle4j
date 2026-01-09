package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "log.txt";

    public static void main(String[] args) {
        PrintWriter log = null;

        try {
            log = new PrintWriter(new FileWriter(LOG_FILE));
            LocalDateTime time = LocalDateTime.now();
            log.println(String.format("Дата: %td.%tm.%tY Время: %tH:%tM:%tS", time, time, time, time, time, time));
            log.println("Запуск игры");
            Scanner scanner = new Scanner(System.in);
            WordleDictionary dictionary = WordleDictionaryLoader.loadDictionary(log, DICTIONARY_FILE);

            WordleGame game = new WordleGame(dictionary, log);

            System.out.println("Добро пожаловать в игру!");

            game.start();

            while (game.isPlaying() && !game.isWin()) {
                System.out.println("Введите ваш ответ:");
                String word = scanner.nextLine();
                if (word.isEmpty()) {
                    word = game.getHint();
                    System.out.println(word);
                }
                game.step(word);
            }

            if (game.isWin()) {
                System.out.println("Поздравляем! Вы угадали!");
            } else {
                System.out.println("У вас закончились попытки! Вы проиграли.");
            }
        } catch (IOException e) {
            System.err.println("Что-то пошло не так. Подробности в логе.");
            log.println(String.format("Ошибка ввода-вывода. Детали: %s.", e.getMessage()));
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }
}
