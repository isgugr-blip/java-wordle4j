package ru.yandex.practicum;

/**
 * Пользовательские исключения для игры Wordle.
 */
public class WordleExceptions {
    /**
     * Выбрасывается, когда слово не найдено в словаре.
     */
    public static class WordNotFoundException extends RuntimeException {
        public WordNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Выбрасывается при передаче некорректного слова.
     */
    public static class InvalidWordException extends RuntimeException {
        public InvalidWordException(String message) {
            super(message);
        }
    }

    /**
     * Выбрасывается при нарушении состояния игры.
     */
    public static class GameStateException extends RuntimeException {
        public GameStateException(String message) {
            super(message);
        }
    }
}