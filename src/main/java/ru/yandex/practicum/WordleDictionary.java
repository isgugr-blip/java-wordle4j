package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс словаря для игры Wordle.
 * Содержит список слов и методы для работы со словами.
 */
public class WordleDictionary {
    private static PrintWriter log;
    private final List<String> words;

    /**
     * Конструктор словаря.
     *
     * @param words Список слов для словаря
     * @param log Писатель для логирования
     * @throws IllegalArgumentException Если список слов пустой
     */
    public WordleDictionary(List<String> words, PrintWriter log) {
        if (words == null) {
            throw new IllegalArgumentException("Список слов не может быть null");
        }
        if (log == null) {
            throw new IllegalArgumentException("Логгер не может быть null");
        }

        // Фильтрация и нормализация слов
        this.words = words.stream()
            .map(WordleDictionary::normalizeWord)
            .filter(word -> !word.isEmpty())
            .distinct()
            .collect(Collectors.toList());

        if (this.words.isEmpty()) {
            throw new IllegalArgumentException("Список слов после нормализации пуст");
        }

        this.log = log;
    }

    /**
     * Нормализация слова для игры.
     *
     * @param word Исходное слово
     * @return Нормализованное слово
     */
    public static String normalizeWord(String word) {
        if (word == null) {
            return "";
        }

        word = word.trim();
        word = word.toLowerCase();
        word = word.replaceAll("\\s", "");
        word = word.replace("ё", "e");

        return word;
    }

    /**
     * Получение списка слов.
     *
     * @return Список слов в словаре
     */
    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    /**
     * Получение случайного слова из словаря.
     *
     * @return Случайное слово
     * @throws IllegalStateException Если словарь пуст
     */
    public String getRandomWord() {
        log.println("Получение случайного слова");

        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст");
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(words.size());
        return words.get(randIndex);
    }

    /**
     * Проверка существования слова в словаре.
     *
     * @param word Слово для проверки
     * @return true, если слово существует
     */
    public boolean isWordExist(String word) {
        if (word == null) {
            return false;
        }

        String normalizedWord = normalizeWord(word);
        log.println("Проверка на существование слова: " + normalizedWord);
        return words.contains(normalizedWord);
    }

    /**
     * Сравнение слов по буквам.
     *
     * @param word Предполагаемое слово
     * @param answer Правильное слово
     * @return Список символов результата сравнения
     * @throws IllegalArgumentException Если длины слов не совпадают
     */
    public static List<Character> checkWordByLetters(String word, String answer) {
        if (word == null || answer == null) {
            throw new IllegalArgumentException("Слова не могут быть null");
        }

        if (word.length() != answer.length()) {
            throw new IllegalArgumentException("Длины слов должны совпадать");
        }

        log.println("Проверка слова по буквам");
        Character[] result = new Character[word.length()];

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (currentChar == answer.charAt(i)) {
                result[i] = '+';
            } else if (answer.indexOf(currentChar) != -1) {
                result[i] = '^';
            } else {
                result[i] = '-';
            }
        }

        return Arrays.asList(result);
    }
}
