package main;

import Exceptions.NotEnoughCardsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/** Author STEVEN
 * main.CardDeck object that holds main.Card objects. Lots of methods for indirect manipulation of the underlying ArrayList as
 * well as various printing, parsing, etc.
 */
public class CardDeck {

    private final ArrayList<Card> cards = new ArrayList<>(52);

    public String getName() {
        return name;
    }

    private final String name;

    public CardDeck(String name) {
        this.name = name;
    }

    /**
     * FOR COMPETITION REPLACE THE cards.add call as comment beside it says. INSTRUCTION #1 - OTHER IS IN CARD CLASS
     */
    public void populate(String deckName) throws IOException {
        File file = new File("resources/" + deckName);
        if (!file.exists())
            throw new FileNotFoundException("Could not find " + file.getPath());

        Scanner scanner = new Scanner(file);
        String input = scanner.nextLine();
        String[] rawCardInput = input.split(",");
        for (String s : rawCardInput) {
            cards.add(0,new Card(parseSuit(s), -1)); //parseValue(rawCardInput[i].substring(1)) <- REPLACE THIS CODE WITH -1
        }
        scanner.close();
    }

    private Suit parseSuit(String s) {
        return switch (s.split("")[0]) {
            case "H" -> Suit.HEARTS;
            case "S" -> Suit.SPADES;
            case "D" -> Suit.DIAMONDS;
            case "C" -> Suit.CLUBS;
            default -> throw new IllegalStateException("Unexpected value: " + s.split("")[0]);
        };
    }

    /** Author Jacob
    Used in order to move the discard pile back to the draw pile, UNDER the cards already there.
    */
    public void appendToIndexOne(Card card) {
        cards.add(0,card);
    }

    private int parseValue(String s) {
        //new code to follow specifications for the competition. Old code commented out below.
        return Integer.parseInt(s);
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name).append(" includes the following: ");
        for (Card card : cards) {
            s.append(card.toString()).append("; ");
        }
        s.append("\n");
        return s.toString();
    }

    public String printCard(int index) {
        if (index > this.size()-1) { return "";}
        else if (index >= 0 && this.size() >= 1) {
            return get(index).getValueLetter() + get(index).getSuitLetter();
        }
        return "XX";
    }

    private Card popCard() throws NotEnoughCardsException {
        if (this.cards.size() != 0) {
            return this.cards.remove(this.cards.size()-1);
        } else {
            throw new NotEnoughCardsException("The deck is empty!");
        }
    }

    public int size() {
        return cards.size();
    }

    public Card get(int index) {
         return cards.get(index);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(int index) { cards.remove(index);
    }

    /** Author Alec
     * @return The index of the last card in the pile.
     */
    public int getLast() {
        return this.size()-1;
    }
    public int getSecondLast(){
        return this.size()-2;
    }

    //Call this function to receive the index of the top card in the deck
    public Integer getBottomFaceCardIndex() {
        return getNumberOfFaceDownCards();
    }

    // Call this function to receive the number of face down cards in the deck
    public Integer getNumberOfFaceDownCards(){
        int number = 0;
        for (Card card : this.cards){
            if(!card.isFaceUp()){
                number++;
            }
        }
        return number;
    }

    /** Author Aya
     * @return all cards in the deck
     */
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public void clearDeck() {
        cards.clear();
    }

    //  Returns a sublist of all the faceup cards in the deck
    public ArrayList<Card> getFaceUpCards(){
        int lastFaceDownIndex = getNumberOfFaceDownCards()-1;
        return (ArrayList<Card>) this.cards.subList(lastFaceDownIndex+1, this.cards.size());
    }

    // Pass this function the index of a card in the deck to see if it matches the given card
    public boolean isCardSuitAndValue(int index, Card card){
        Card deckCard = this.cards.get(index);
        return deckCard.getValue() == card.getValue() &&
                deckCard.getSuit() == card.getSuit();
    }


    // Aya: Call this function to check if popping the topcard will free a downcard
    public boolean canFreeDownCard(){

        return !(getNumberOfFaceDownCards() == 0);
    }

    public ArrayList<Card> getAllCardsOfValue(int cardValue){
        ArrayList<Card> cards = new ArrayList<>();
        for(Card card : this.cards){
            if(card.getValue() == cardValue){
                cards.add(card);
            }
        }
        return cards;
    }
}
