package main;

public class Move {
    private CardDeck sourceDeck;
    private CardDeck destinationDeck;
    private int index;

    private int priority;

    public Move(CardDeck source, CardDeck dest, int index, int priority) {
        this.sourceDeck = source;
        this.destinationDeck = dest;
        this.index = index;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDestinationDeck(CardDeck destinationDeck) {
        this.destinationDeck = destinationDeck;
    }

    public CardDeck getDestinationDeck() {
        return destinationDeck;
    }

    public void setSourceDeck(CardDeck sourceDeck) {
        this.sourceDeck = sourceDeck;
    }

    public CardDeck getSourceDeck() {
        return sourceDeck;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return ("Source: " + sourceDeck.getName() + ", Destination: " + destinationDeck.getName() + ", Index: " + index + ", Priority: " + priority);
    }
}
