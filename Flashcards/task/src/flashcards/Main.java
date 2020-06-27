package flashcards;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static boolean isExit = false;
    static CardList flashCards = new CardList();
    static Statistics stats = new Statistics();


    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                int cardsCount = flashCards.importFrom(args[i + 1]);
                if (cardsCount == -1) cardsCount = 0;
                printAndSave(cardsCount + " cards have been loaded.\n");
                break;
            }
        }

        while (!isExit) {
            printAndSave("\nInput the action (add, remove, import, export, ask, exit):\n");
            String command = scanAndSave();


            switch (command) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    printAndSave("The card:\n");
                    String term = scanAndSave();
                    stats.removeMistake(term);
                    boolean isRemoved = flashCards.remove(term);
                    printAndSave(isRemoved ?
                            "The card has been removed.\n" :
                            String.format("Can't remove \"%s\": there is no such card.\n", term));
                    break;
                case "import":
                    printAndSave("File name:\n");
                    String fileName = scanAndSave();
                    int cardsCount = flashCards.importFrom(fileName);
                    printAndSave(cardsCount == -1 ?
                            "File not found.\n" :
                            cardsCount + " cards have been loaded.\n");
                    break;
                case "export":
                    printAndSave("File name:\n");
                    String fName = scanAndSave();
                    int cCount = flashCards.exportTo(fName);
                    printAndSave(cCount + " cards have been saved.\n");
                    break;
                case "ask":
                    printAndSave("How many times to ask?\n");
                    int times = Integer.parseInt(scanAndSave());

                    if (flashCards.size() == 0) {
                        printAndSave("ERROR! No cards!\n");
                        break;
                    }

                    for (int i = 0; i < times; i++) {
                        checkAnswer(flashCards.getRandomCard());
                    }
                    break;
                case "hardest card":
                    printAndSave(stats.getHardestCard());
                    break;
                case "log":
                    printAndSave("File name:\n");
                    String fileN = scanAndSave();
                    boolean isLogged = stats.log(fileN);
                    if (isLogged) {
                        printAndSave("The log has been saved.\n");
                    }
                    break;
                case "reset stats":
                    stats.reset();
                    printAndSave("Card statistics has been reset.\n");
                    break;
                case "exit":
                    isExit = true;
                    printAndSave("Bye bye!\n");
                    for (int i = 0; i < args.length; i++) {
                        if (args[i].equals("-export")) {
                            int count = flashCards.exportTo(args[i + 1]);
                            if (count != -1) {
                                printAndSave(count + " cards have been saved.\n");
                            }
                            break;
                        }
                    }
                    break;
                default:
                    printAndSave("Wrong command...\n");
            }
        }
    }

    public static void addCard() {
        printAndSave("The card:\n");
        String term = readTerm();
        if (term == null) return;

        printAndSave("The definition of the card:\n");
        String definition = readDefinition();
        if (definition == null) return;

        FlashCard newCard = new FlashCard(term, definition);
        flashCards.add(newCard);
        printAndSave(String.format("The pair (%s) has been added.\n", newCard.toString()));
    }

    private static String readTerm() {
        String term = scanAndSave();

        for (int i = 0; i < flashCards.size(); i++) {
            FlashCard card = flashCards.get(i);
            if (card.getTerm().equals(term)) {
                printAndSave(String.format("The card \"%s\" already exists.\n", term));
                return null;
            }
        }

        return term;
    }


    private static String readDefinition() {
        String definition = scanAndSave();

        for (int i = 0; i < flashCards.size(); i++) {
            FlashCard card = flashCards.get(i);
            if (card.getDefinition().equals(definition)) {
                printAndSave(String.format("The definition \"%s\" already exists.\n", definition));
                return null;
            }
        }

        return definition;
    }

    public static void checkAnswer(FlashCard card) {
        printAndSave(String.format("Print the definition of \"%s\":\n", card.getTerm()));
        String answer = scanAndSave();

        if (card.isRight(answer)) {
            printAndSave("Correct answer.\n");
        } else {
            stats.addMistake(card.getTerm());
            printAndSave(String.format("Wrong answer. The correct one is \"%s\"", card.getDefinition()));

            for (int i = 0; i < flashCards.size(); i++) {
                FlashCard otherCard = flashCards.get(i);
                if (otherCard.isRight(answer)) {
                    printAndSave(String.format(", you've just written the definition of \"%s\".\n", otherCard.getTerm()));
                    return;
                }
            }

            printAndSave(".\n");
        }
    }

    static void printAndSave(String text) {
        System.out.print(text);
        stats.saveActivity(text);
    }

    static String scanAndSave() {
        String command = scanner.nextLine();
        stats.saveActivity(command);
        return command;
    }
}
