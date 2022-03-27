package com.company;

public class Move {
    private CardDeck sourceDeck;
    private CardDeck destinationDeck;
    private int index;

    public Move(CardDeck source, CardDeck dest, int index) {
        this.sourceDeck = source;
        this.destinationDeck = dest;
        this.index = index;
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
}
