package InitialSetup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TasteTestRepository {
    private static final String QUESTIONS_FILE_PATH = "src/assets/Flie(txt)/whisky_test_questions_en.txt";
    private static final String RESULTS_FILE_PATH = "src/assets/Flie(txt)/whisky_test_results_en.txt";

    private TasteTestRepository() {
    }

    public static List<TasteQuestion> loadQuestions() {
        ArrayList<TasteQuestion> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(QUESTIONS_FILE_PATH))) {
            String line;
            boolean skippedHeader = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                if (!skippedHeader) {
                    skippedHeader = true;
                    continue;
                }

                String[] values = line.split("\\|", -1);
                if (values.length < 5) {
                    continue;
                }

                questions.add(new TasteQuestion(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        Arrays.stream(values[4].split(","))
                                .map(String::trim)
                                .filter(value -> !value.isBlank())
                                .toList()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public static Map<String, TasteResultProfile> loadResults() {
        LinkedHashMap<String, TasteResultProfile> results = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(RESULTS_FILE_PATH))) {
            String line;
            boolean skippedHeader = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                if (!skippedHeader) {
                    skippedHeader = true;
                    continue;
                }

                String[] values = line.split("\\|", -1);
                if (values.length < 4) {
                    continue;
                }

                results.put(values[0], new TasteResultProfile(
                        values[0],
                        values[1],
                        values[2],
                        values[3]
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
