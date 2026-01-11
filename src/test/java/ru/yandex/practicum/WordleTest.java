package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private static PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeAll
    static void loggingStart() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void setUp() {
        List<String> wordList = Arrays.asList(
                "бобер", "кошка", "какао", "лимон", "груша", "пилон"
        );
        dictionary = new WordleDictionary(wordList, log);
        game = new WordleGame(dictionary, log);
    }

    @Test
    void testContainsWord() {
        assertTrue(dictionary.getWords().contains("бобер"));
        assertFalse(dictionary.getWords().contains("БОБЁР"));
        assertFalse(dictionary.getWords().contains("Лимон"));
        assertTrue(dictionary.getWords().contains("лимон"));
    }

    @Test
    void testGetRandomWord() {
        String randomWold = dictionary.getRandomWord();
        assertEquals(5, randomWold.length());
        assertTrue(dictionary.getWords().contains(randomWold));
    }

    @Test
    void testCompareAllCharCorrect() {
        List<Character> result = WordleDictionary.checkWordByLetters("папка", "папка");
        assertEquals("+++++", result.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    @Test
    void testCompareSomeCharCorrect() {
        List<Character> result = WordleDictionary.checkWordByLetters("палка", "полка");
        assertEquals("+^+++", result.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    @Test
    void testCompareNotCharCorrect() {
        List<Character> result = WordleDictionary.checkWordByLetters("лапша", "лимон");
        assertEquals("+----", result.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    @Test
    void testCorrectGame() {
        WordleGame game = new WordleGame(dictionary, log);
        assertNotNull(game);
        assertEquals(0, game.getStep());
    }

    @Test
    void testDictionaryCaseSensitiveContains() {
        List<String> wordList = Arrays.asList("Бобер", "Кошка");
        WordleDictionary sensitiveDict = new WordleDictionary(wordList, log);
        assertTrue(sensitiveDict.getWords().contains("бобер"));
        assertFalse(sensitiveDict.getWords().contains("Бобер"));
    }

    @Test
    void testGameStartAndPlay() {
        game.start();
        assertTrue(game.isPlaying());
        assertFalse(game.isWin());
        assertEquals(0, game.getStep());
    }


    @Test
    void testGameHints() {
        game.start();
        assertFalse(game.getHints().isEmpty());

        game.step("лимон");
        assertTrue(game.getHints().size() <= dictionary.getWords().size());
    }

    @Test
    void testInvalidWordInGame() {
        game.start();
        assertThrows(IllegalArgumentException.class, () -> game.step("несуществующеeslово"));
    }

    @Test
    void testGameStateValidation() {
        assertThrows(IllegalStateException.class, () -> game.step("лимон"));
    }
}
