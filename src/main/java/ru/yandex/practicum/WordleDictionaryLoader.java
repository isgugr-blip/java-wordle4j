package ru.yandex.practicum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public static WordleDictionary loadDictionary(PrintWriter log, String dictFileName) {
        List<String> words = new ArrayList<>();
        log.println("Загрузка словаря");
        try (BufferedReader reader = new BufferedReader(new FileReader(dictFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = WordleDictionary.normalizeWord(line);
                if (line.length() == 5) {
                    words.add(line);
                }
            }
            if (words.isEmpty()) {
                throw new DictionaryException("Словарь пуст, нет подходящих слов");
            }
            WordleDictionary dictionary = new WordleDictionary(words, log);
            log.println("Загружено " + dictionary.getWords().size() + " слов");
            return dictionary;
        } catch (FileNotFoundException e) {
            log.println("Не найден файл словаря");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.println("Ошибка ввода-вывода при работе с словарем. Подробности: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
