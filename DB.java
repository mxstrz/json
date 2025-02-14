package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final String FILE_NAME = Paths.get("data.json").toAbsolutePath().toString();
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void save(UserData userData) {
        List<UserData> userDataList = read();
        userDataList.add(userData);

        try {
            objectMapper.writeValue(new File(FILE_NAME), userDataList);
            System.out.println("Дані успішно записані у файл: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Помилка запису даних у файл.");
            e.printStackTrace();
        }
    }

    public static List<UserData> read() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Файл ще не створено.");
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, UserData.class));
        } catch (IOException e) {
            System.out.println("Помилка читання файлу.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}