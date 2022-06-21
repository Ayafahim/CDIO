package main;

import java.io.IOException;

/** Author STEVEN
 * main.Card object to be used in Arraylists representing the board.
 */
public class Card {

    private Suit suit;

    public void setValue(int value) {
        this.value = value;
    }

    private int value;
    private boolean faceUp;


    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        faceUp = false;
    }

    public void setFaceUp(boolean up, Main imageRecInput) throws IOException {
        if (!up) {
            this.faceUp = false;
        }
        else {
            String input = imageRecInput.test();
            this.setSuit(parseSuit(input));
            this.setValue(Integer.parseInt(input.substring(1)));
            this.faceUp = true;
            System.out.println("Card set face up: " + this.suit + " " + this.value + " " + this.suit);
        }








    }
    private Suit parseSuit(String s) {
        return switch (s.substring(0,1)) {
            case "H" -> Suit.HEARTS;
            case "S" -> Suit.SPADES;
            case "C" -> Suit.CLUBS;
            case "D" -> Suit.DIAMONDS;
            default -> throw new IllegalStateException("Unexpected value: " + s.split("")[0]);
        };
    }

    public Suit getSuit(){return this.suit;}
    public String getSuitLetter(){if (!faceUp) return "]"; return parseSuitReverse(suit);}
    public int getValue(){return this.value;}
    public void setSuit(Suit suit){
        this.suit = suit;
    }
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
        //New code to follow the competition specifications. Old code below.
        return String.valueOf(n);

        /*return switch (n) {
            case 1 -> "A";
            case 10 -> "T";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(n);
        };*/
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
