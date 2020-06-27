package flashcards;

public class FlashCard {
    private String term;
    private String definition;

    public FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public boolean isRight(String answer) {
        return answer.equals(definition);
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "\"" + term + "\":\"" + definition + "\"";
    }
}
