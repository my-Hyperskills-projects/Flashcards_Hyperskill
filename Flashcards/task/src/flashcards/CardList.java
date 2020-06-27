package flashcards;

import com.sun.nio.sctp.PeerAddressChangeNotification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CardList {
    private ArrayList<FlashCard> flashCards;

    public CardList() {
        flashCards = new ArrayList<>();
    }

    void add(FlashCard card) {
        flashCards.add(card);
    }

    boolean remove(String term) {
        for (int i = 0; i < flashCards.size(); i++) {
            if (flashCards.get(i).getTerm().equals(term)) {
                flashCards.remove(i);
                return true;
            }
        }
        return false;
    }

    int importFrom(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] data = line.split(":");
                String term = data[0];
                String definition = data[1];
                int mistakes = Integer.parseInt(data[2]);
                FlashCard card = new FlashCard(term, definition);

                if (flashCards.size() == 0) {
                    flashCards.add(card);
                } else {
                    for (int i = 0; i < flashCards.size(); i++) {
                        if (flashCards.get(i).getTerm().equals(term)) {
                            flashCards.set(i, card);
                            break;
                        } else if (i == (flashCards.size() - 1)) {
                            flashCards.add(card);
                        }
                    }
                }

                Main.stats.addMistake(term, mistakes);
            }
            return lineCount;

        } catch (IOException e) {
            return -1;
        }
    }

    int exportTo(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (FlashCard card : flashCards) {
                String text = card.getTerm() + ":" + card.getDefinition() + ":" + Main.stats.getMisCount(card.getTerm()) + "\n";
                writer.write(text);
            }
            return flashCards.size();
        } catch (IOException e) {
            return -1;
        }
    }

    FlashCard getRandomCard() {
        Random random = new Random();
        int index = random.nextInt(flashCards.size());
        return flashCards.get(index);
    }

    FlashCard get(int index) {
        return flashCards.get(index);
    }

    int size() {
        return flashCards.size();
    }
}
