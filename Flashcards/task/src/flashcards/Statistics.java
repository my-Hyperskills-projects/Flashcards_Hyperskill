package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {
    ArrayList<String> logs;
    HashMap<String, Integer> mistakes;

    public Statistics() {
        logs = new ArrayList<>();
        mistakes = new HashMap<>();
    }

    void removeMistake(String term) {
        if (mistakes.containsKey(term)) {
            mistakes.remove(term);
        }
    }

    void addMistake(String term) {
        if (mistakes.containsKey(term)) {
            int misCount = mistakes.remove(term) + 1;
            mistakes.put(term, misCount);
        } else {
            mistakes.put(term, 1);
        }
    }

    void addMistake(String term, int misCount) {
        if (mistakes.containsKey(term)) {
            mistakes.remove(term);
        }
        mistakes.put(term, misCount);
    }

    int getMisCount(String term) {
        if (!mistakes.containsKey(term)) {
            return 0;
        }
        return mistakes.get(term);
    }

    String getHardestCard() {
        if (mistakes.isEmpty()) {
            return "There are no cards with errors.\n";
        }

        String hardestCard = "";
        int misCount = 0;
        for (var card : mistakes.entrySet()) {
            if (card.getValue() > misCount) {
                hardestCard = "\"" + card.getKey() + "\"";
                misCount = card.getValue();
            } else if (card.getValue() == misCount) {
                hardestCard += ", \"" + card.getKey() + "\"";
            }
        }
        return "The hardest card is " + hardestCard + ". You have " + misCount + " errors answering it.\n";
    }

    void saveActivity(String activity) {
        logs.add(activity);
    }

    boolean log(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String log : logs) {
                writer.write(log + "\n");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    void reset() {
        logs.clear();
        mistakes.clear();
    }
}