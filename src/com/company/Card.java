package com.company;

public class Card {

    private final Suit suit;
    private final int value;
    private boolean faceUp;


    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        faceUp = false;
    }

    public void setFaceUp(boolean up){this.faceUp = up;}

    public Suit getSuit(){return this.suit;}
    public String getSuitLetter(){if (!faceUp) return "]"; return parseSuitReverse(suit);}
    public int getValue(){return this.value;}
    public String getValueLetter() {if (!faceUp) return "["; return parseValueReverse(value);}
    private String parseSuitReverse(Suit suit) {
        return switch (suit) {
            case HEARTS -> "H";
            case SPADES -> "S";
            case DIAMONDS -> "D";
            case CLUBS -> "C";
        };
    }

    public String parseValueReverse(int n) {
        return switch (n) {
            case 1 -> "A";
            case 10 -> "T";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(n);
        };
    }

    public boolean isRed() {
        return (this.suit == Suit.HEARTS || this.suit == Suit.DIAMONDS);
    }
    public boolean isBlack() {
        return (this.suit == Suit.SPADES || this.suit == Suit.CLUBS);
    }
    public boolean isFaceUp(){return this.faceUp;}

    public String toString() {
        return value + " of " + suit.toString();
    }

}