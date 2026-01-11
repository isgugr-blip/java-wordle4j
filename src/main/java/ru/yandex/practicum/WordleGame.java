package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс игры Wordle.
 * Управляет состоянием игры, логикой проверки слов и подсказками.
 */
public class WordleGame {
    /** Максимальное количество попыток */
    private static final int MAX_ATTEMPTS = 5;

    /** Логгер для записи событий */
    private final PrintWriter log;

    /** Правильное слово */
    private String answer;

    /** Состояние победы */
    private boolean isWin = false;

    /** Состояние игры */
    private boolean isPlaying = false;

    /** Текущий шаг (попытка) */
    private int step;

    /** Словарь для игры */
    private final WordleDictionary dictionary;

    /** Список подсказок */
    private List<String> hints;

    /** Найденные позиции символов */
    private final boolean[] foundCharacters;

    /** Существующие символы */
    private final Set<Character> existingCharacters;

    /** Неправильные символы */
    private final Set<Character> wrongCharacters;

    /**
     * Конструктор игры.
     *
     * @param dictionary Словарь слов
     * @param log Логгер для записи событий
     */
    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        this.foundCharacters = new boolean[5];
        this.existingCharacters = new HashSet<>();
        this.wrongCharacters = new HashSet<>();
    }

    /**
     * Проверка, выиграна ли игра.
     *
     * @return true, если игра выиграна
     */
    public boolean isWin() {
        return isWin;
    }

    /**
     * Проверка, идет ли игра.
     *
     * @return true, если игра продолжается
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Получение текущих подсказок.
     *
     * @return Список подсказок
     */
    public List<String> getHints() {
        return new ArrayList<>(hints);
    }

    /**
     * Получение текущего шага игры.
     *
     * @return Номер текущей попытки
     */
    public int getStep() {
        return step;
    }

    /**
     * Начало игры.
     */
    public void start() {
        resetGameState();
        answer = dictionary.getRandomWord();
        hints = dictionary.getWords();
        log.println("Игра началась. Загадано слово: " + answer);
        isPlaying = true;
    }

    /**
     * Обработка шага игры.
     *
     * @param word Слово, введенное игроком
     * @throws IllegalStateException Если игра не началась
     */
    public void step(String word) {
        validateGameState();

        log.println("Введено слово: " + word + ", текущая попытка: " + step);

        if (!dictionary.isWordExist(word)) {
            log.println("Введено несуществующее слово");
            throw new IllegalArgumentException("Слово не существует в словаре");
        }

        if (word.equals(answer)) {
            log.println("Игрок угадал слово");
            isWin = true;
            isPlaying = false;
            return;
        }

        List<Character> checkResult = WordleDictionary.checkWordByLetters(word, answer);
        System.out.println(checkResult.stream().map(String::valueOf).collect(Collectors.joining("")));
        updateHints(word, checkResult);
        log.println("Результат проверки: " +
            checkResult.stream().map(String::valueOf).collect(Collectors.joining("")));

        step++;

        if (step >= MAX_ATTEMPTS) {
            log.println("Попытки закончились");
            isPlaying = false;
        }
    }

    /**
     * Получение подсказки.
     *
     * @return Случайная подсказка
     */
    public String getHint() {
        if (hints.isEmpty()) {
            throw new IllegalStateException("Нет доступных подсказок");
        }

        if (hints.size() == 1) {
            return hints.get(0);
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(hints.size());
        return hints.get(randIndex);
    }

    /**
     * Обновление списка подсказок.
     *
     * @param word Текущее слово
     * @param checkResult Результат проверки слова
     */
    public void updateHints(String word, List<Character> checkResult) {
        log.println("Обновление подсказок");
        hints.remove(word);
        processCheckResult(word, checkResult);

        hints = hints.stream()
            .filter(hint -> containsFoundLetters(word, hint))
            .filter(this::notContainsWrongLetters)
            .filter(this::containsExistingLetters)
            .collect(Collectors.toList());
    }

    /**
     * Проверка отсутствия неправильных букв.
     *
     * @param hint Слово-подсказка
     * @return true, если в слове нет неправильных букв
     */
    public boolean notContainsWrongLetters(String hint) {
        return wrongCharacters.stream()
            .noneMatch(letter -> hint.indexOf(letter) != -1);
    }

    /**
     * Проверка наличия существующих букв.
     *
     * @param hint Слово-подсказка
     * @return true, если в слове есть все существующие буквы
     */
    public boolean containsExistingLetters(String hint) {
        return existingCharacters.stream()
            .allMatch(letter -> hint.indexOf(letter) != -1);
    }

    /**
     * Проверка наличия найденных букв.
     *
     * @param word Исходное слово
     * @param hint Слово-подсказка
     * @return true, если подсказка соответствует найденным буквам
     */
    public boolean containsFoundLetters(String word, String hint) {
        for (int i = 0; i < foundCharacters.length; i++) {
            if (foundCharacters[i] && word.charAt(i) != hint.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Обработка результатов проверки слова.
     *
     * @param word Введенное слово
     * @param checkResult Результат проверки
     */
    public void processCheckResult(String word, List<Character> checkResult) {
        log.println("Обработка результатов проверки");
        for (int i = 0; i < checkResult.size(); i++) {
            switch (checkResult.get(i)) {
                case '^':
                    existingCharacters.add(word.charAt(i));
                    break;
                case '-':
                    wrongCharacters.add(word.charAt(i));
                    break;
                case '+':
                    foundCharacters[i] = true;
                    existingCharacters.add(word.charAt(i));
                    break;
            }
        }
    }

    /**
     * Сброс состояния игры.
     */
    private void resetGameState() {
        isWin = false;
        step = 0;
        Arrays.fill(foundCharacters, false);
        existingCharacters.clear();
        wrongCharacters.clear();
    }

    /**
     * Проверка состояния игры перед шагом.
     *
     * @throws IllegalStateException Если игра не началась
     */
    private void validateGameState() {
        if (!isPlaying) {
            throw new IllegalStateException("Игра не начата или уже завершена");
        }
    }
}
