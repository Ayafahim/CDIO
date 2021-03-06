package main;

import java.io.IOException;
import java.util.*;


public class Board {

    public Main imageRecInput = new Main(); //For image rec input.

    //Initialize all the piles contained in the board
    public CardDeck initialPile = new CardDeck("Initial Deck"); //id = "deck"
    public CardDeck drawPile = new CardDeck("Draw Pile"); // "draw"
    public CardDeck discardPile = new CardDeck("Discard Pile"); //id = "discard"
    public CardDeck pile1 = new CardDeck("Pile 1"); // "1"
    public CardDeck pile2 = new CardDeck("Pile 2"); // "2"
    public CardDeck pile3 = new CardDeck("Pile 3"); // "3"
    public CardDeck pile4 = new CardDeck("Pile 4"); // "4"
    public CardDeck pile5 = new CardDeck("Pile 5"); // "5"
    public CardDeck pile6 = new CardDeck("Pile 6"); // "6"
    public CardDeck pile7 = new CardDeck("Pile 7"); // "7"
    public CardDeck heartsPile = new CardDeck("Hearts Foundation"); // "hearts"
    public CardDeck spadesPile = new CardDeck("Spades Foundation"); // "spades"
    public CardDeck diamondsPile = new CardDeck("Diamonds Foundation"); // "diamonds"
    public CardDeck clubsPile = new CardDeck("Clubs Foundation"); // "clubs"
    AI ai = new AI(new Search(this), this);

    public ArrayList<CardDeck> drawAndDiscardPiles = new ArrayList<>(Arrays.asList(drawPile,discardPile));
    public ArrayList<CardDeck> numberPiles = new ArrayList<>(Arrays.asList(pile1,pile2,pile3,pile4,pile5,pile6,pile7));
    public ArrayList<CardDeck> foundationPiles = new ArrayList<>(Arrays.asList(heartsPile,spadesPile,diamondsPile,clubsPile));
    public ArrayList<CardDeck> searchPiles = new ArrayList<>(Arrays.asList(discardPile,pile1,pile2,pile3,pile4,pile5,pile6,pile7));

    /** Author STEVEN
     *  Parses text input for manual use of the program
     *  Cheatsheet -> piles 1-7, foundations 8-11, draw/discard 12-13, initial -1
     */
    public void parseInput(String input) throws Exception {
        try {
            switch (input) {
                case "goodbye": {
                    return;
                }
                case "shuffle": {
                    drawPile.shuffleDeck();
                    break;
                }
                case "":
                case "ai": {
                    //System.out.println("AI in testing phase :)");
                    ai.think();
                    break;
                }
                case "debug": { //For debugging purposes. Change the code inside this block as needed to test parts of the AI.
                    System.out.println("Testing NumberToNumber :)");
                    ai.moveNumberToNumber();
                    System.out.println(ai.movesList);

                    break;
                }
                case "panic": {
                    ai.panicMode = !ai.panicMode; //Flips panic mode like a switch :D
                    ai.printPanicState();
                }
                case "draw": {
                    draw3Cards();
                    break;
                }
                case "restart": {
                    initialPile.clearDeck();
                    drawPile.clearDeck();
                    pile1.clearDeck();
                    pile2.clearDeck();
                    pile3.clearDeck();
                    pile4.clearDeck();
                    pile5.clearDeck();
                    pile6.clearDeck();
                    pile7.clearDeck();
                    heartsPile.clearDeck();
                    spadesPile.clearDeck();
                    diamondsPile.clearDeck();
                    clubsPile.clearDeck();
                    this.initialPile.populate("cards.txt"); //Needs redo in order to choose which deck of cards is loaded in
                    this.initialPile.shuffleDeck();
                    this.initialPopulateBoard();
                    break;
                }
                default: {
                    StringTokenizer st = new StringTokenizer(input, " ");
                    String s = st.nextToken();
                    String d = st.nextToken();
                    String i = st.nextToken();
                    if (i.equals("last")) {
                        i = String.valueOf(getDeck(s).size() - 1);
                    }
                    Move move = new Move(getDeck(s), getDeck(d), Integer.parseInt(i),1000);//ToDo FIX PRIORITY
                    System.out.println("main.Move is: " + move);
                    attemptMove(move);
                }
            }
            /*
             * Quick add from Jacob
             * Edit Aya: just changed it to print exception
             */
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** Author STEVEN
     Flips newly revealed cards to face-up.
     */
    public void updateBoardState() throws IOException {
        for (int i = 0; i < numberPiles.size(); i++) {
            if (numberPiles.get(i).size() > 0) {

                numberPiles.get(i).get(numberPiles.get(i).size()-1).setFaceUp(true,imageRecInput); //update the 7 number piles
            }
        }
        if (discardPile.size() > 0) {
            discardPile.get(discardPile.size() - 1).setFaceUp(true,imageRecInput); //update top of the discard pile
        }
    }

    /**
     * Author STEVEN
     * @param suit a given suit.
     * @return Returns the foundation matching the given suit.
     */
    public CardDeck getSuitPile(Suit suit) {
        return switch (suit) {
            case HEARTS -> heartsPile;
            case SPADES -> spadesPile;
            case DIAMONDS -> diamondsPile;
            case CLUBS -> clubsPile;
        };
    }

    /** Author STEVEN
     * Given a move object, checks the source & destination piles and attempts to move it there.
     */
    public boolean attemptMove(Move move) throws IOException {
        CardDeck s = move.getSourceDeck();
        CardDeck d = move.getDestinationDeck();
        int x = move.getIndex();

        //Only for Draw Move
        if (s == drawPile && d == discardPile) {
            draw3Cards();
            return true;
        }

        //Attempts to move card(s) from number pile to number pile.
        if (isNumberPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from number pile to foundation
        else if (isNumberPile(s) && isFoundationPile(d)) {
            if (canMoveToFoundation(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from foundation to number pile
        else if (isFoundationPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }

        else if (s == discardPile && isFoundationPile(d) || isNumberPile(d)) {
            if (x == s.size()-1) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //If the move wasnt legal, and wasnt executed
        System.out.println("Illegal move sent to attemptMove() -> Source: " + s.toString() + " Destination: " + d.toString() + " Index: " + x + " Card: " + s.get(x));
        return false;
    }

    /** Author STEVEN
     * Check if the index card in the source pile is allowed to be moved to the destination number pile.
     * Ayas edit: i made changes so that we wont get stuck in a loop
     */
    public boolean canMoveToNumberPile(CardDeck source, CardDeck destination, int index) {
        boolean value = false;
        boolean suit = false;
        boolean isFaceUp = false;
        boolean legalNumberOfCards = true;
        //Checks for attempting a move of king to empty pile.
        if (destination.size() == 0 ) {
            if (source.get(index).getValue() == 13) {
                if (index > 0) {
                    if (source.get(index).isFaceUp()) {
                        return true;
                    }
                }
            }
            else return false;
        }
        //Source number value is 1 less than destination number.
        if (destination.size() > 0) {
            if (destination.get(destination.size() - 1).getValue() - source.get(index).getValue() == 1) {
                value = true;
            }
            //Source color is opposite of destination color
            if ((source.get(index).isRed() && destination.get(destination.size() - 1).isBlack()) || (source.get(index).isBlack() && destination.get(destination.size() - 1).isRed())) {

                //makes sure we dont end up en loop transfering the same cards between to cards with same color
                //only interested if index of card isn't 0 and the card isn't in discard pile
                if (index > 0 && source != discardPile) {
                    if (source.get(index - 1).isRed() && destination.get(destination.size() - 1).isRed() || source.get(index - 1).isBlack() && destination.get(destination.size() - 1).isBlack()) {
                        suit = false;
                    }
                    else {
                        suit = true;
                    }
                } else {
                    suit = true;
                }
            }
            //Both cards must be face-up for the move to make any sense
            isFaceUp = areFaceUp(source, destination, index);
            //Can only move multiple cards if the source deck is a number pile.
            if (source.size() - 1 > index && !isNumberPile(source)) {
                legalNumberOfCards = false;
            }
        }
        //System.out.println("canMoveToNumberPile boolean results -> " + value + " " + suit + " " + isFaceUp + " " + legalNumberOfCards);
        return (value && suit && isFaceUp && legalNumberOfCards);
    }

    /** Author STEVEN
     * Check if the index card in the source pile is allowed to be moved to the destination foundation pile.
     */
    public boolean canMoveToFoundation(CardDeck source, CardDeck destination, int index) {
        boolean legalIndex = false;
        Suit suit = source.get(index).getSuit();
        boolean matchingSuit = false;
        boolean matchingValue = false;
        boolean isFaceUp;

        //You can only move the last card in a pile to a foundation (1 at a time)
        if (source.size() - 1 == index) {
            legalIndex = true;
        }
        //Suits must match on source & destination
        if ((suit == Suit.HEARTS && destination == heartsPile)
                || (suit == Suit.SPADES && destination == spadesPile)
                || (suit == Suit.DIAMONDS && destination == diamondsPile)
                || (suit == Suit.CLUBS && destination == clubsPile)) {
            matchingSuit = true;
        }
        //Source number must be 1 higher than destination number
        if (destination.size() > 0) {
            if (source.get(index).getValue() - destination.get(destination.size() - 1).getValue() == 1) {
                matchingValue = true;
            }
        }
        //Otherwise, the source must be an Ace and the destination pile empty
        else if (destination.size() == 0) {
            if (source.get(index).getValue() == 1) {
                matchingValue = true;
            }
        }
        //Double-check that both cards are face-up.
        isFaceUp = areFaceUp(source, destination, index);

        return (legalIndex && matchingSuit && matchingValue && isFaceUp);
    }

    /** Author STEVEN
     * Simply moves a card or several from 1 deck to another. Checks for legality of moves must be done before calling
     * this function. The function moves every card from the given index to the end of the list, in order.
     */
    public void moveCardDeckToDeck(CardDeck source, CardDeck destination, int index, boolean flipFaceUp) throws IOException {

        while (source.size() > index) {
            destination.add(source.get(index));
            source.remove(index);
            destination.get(destination.size() - 1).setFaceUp(flipFaceUp,imageRecInput);
        }
    }

    /** Author STEVEN
     *  Draws a card from the deck and puts it in the discard pile face-up. If the draw pile is empty, fills the draw
     *  pile with the discard pile, retaining their natural order.
     */
    public void draw3Cards() throws IOException {
        //New code to fit the competition requirements. Old code commented out below.
        CardDeck draw = drawPile;
        CardDeck discard = discardPile;
        if (draw.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                //add card to discard in modified order and remove it from draw
                //modified order simulates flipping 3 cards simultaneously
                discard.add(draw.get(draw.size()-1));
                draw.remove(draw.size()-1);
                //discard.get(discard.size()-1).setFaceUp(true,imageRecInput);
            }
        }
        else {
            shuffleDiscardIntoDraw();
            System.out.println("The last DRAW is instead a STOCK move!");
            //does not call the method again as they're considered separate moves.
        }

    }

    /** Author STEVEN
     * Moves the cards one by one from the bottom of the discard pile to the bottom of the draw pile, as outlined
     * in the competition rules.
     */
    public void shuffleDiscardIntoDraw() {
        for (int i = discardPile.size(); i > 0; i--) {
            drawPile.appendToIndexOne(discardPile.get(0));
            discardPile.remove(0);
        }
    }

    /** Author STEVEN
     * Checks if a given card is face-up, allowing it to be moved from its pile.
     */
    public boolean areFaceUp(CardDeck source, CardDeck destination, int index) {
        return source.get(index).isFaceUp();
    }
    
    public boolean numberOfCardsMovedIsLegal(CardDeck source, CardDeck destination, int index) {
        boolean isLegal = false;
        //If moving 1 card, move is legal
        if (index == source.size() - 1) {
            isLegal = true;
        }
        //If moving multiple cards, both source & destination must be number piles.
        else if (isNumberPile(source) && isNumberPile(destination)) {
            isLegal = true;
        }
        return isLegal;
    }

    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isNumberPile(CardDeck source) {
        return source == pile1 || source == pile2 || source == pile3 || source == pile4 || source == pile5 || source == pile6 || source == pile7;
    }

    public boolean isDrawPile(CardDeck source) {
        return source == drawPile;
    }
    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isFoundationPile(CardDeck source) {
        return source == heartsPile || source == spadesPile || source == diamondsPile || source == clubsPile;
    }


    /** Author STEVEN
     * Allows for refering a deck by its string value. Useful for looping through the 7 number piles where the loop
     * index corresponds to the pile number.
     */
    public CardDeck getDeck(String input) {
        return switch (input) {
            case "-1", "deck" -> initialPile;
            case "12", "draw" -> drawPile;
            case "13", "discard" -> discardPile;
            case "1" -> pile1;
            case "2" -> pile2;
            case "3" -> pile3;
            case "4" -> pile4;
            case "5" -> pile5;
            case "6" -> pile6;
            case "7" -> pile7;
            case "8", "hearts" -> heartsPile;
            case "9", "spades" -> spadesPile;
            case "10", "diamonds" -> diamondsPile;
            case "11", "clubs" -> clubsPile;
            default -> null; //Asking for a non-existing pile will cause a null-pointer exception from this.
        };
    }

    /** Author STEVEN
     * Creates the board using the initial card deck. Make sure the deck is shuffled beforehand.
     */
    public void initialPopulateBoard() throws IOException {
        imageRecInput.initiate();
        //Fills each card pile with 1-7 cards, respectively. Then it flips the last card face up.
        for (int i = 1; i <= 7; i++) {
            for (int j = i; j <= 7; j++) {
                moveCardDeckToDeck(initialPile, getDeck(Integer.toString(j)),
                        initialPile.getLast(), false);
                if (i == j) {
                    getDeck(Integer.toString(i)).get(i - 1).setFaceUp(true, imageRecInput);
                    //System.out.println("card set face up in -> " + getDeck(Integer.toString(i)));
                }

            }
        }
            moveCardDeckToDeck(initialPile, drawPile, 0, false);
        }
    /** Author STEVEN
     * Prints out a TUI representation of the board.
     */
    public void printBoard() {
        String tab = "\t";
        String dtab = "\t\t";

        // Create new super print method with formatting
        System.out.println("DR" + tab + drawPile.printCard(drawPile.size()-1) + dtab + "FH" + tab + "FS" + tab + "FD" + tab + "FC" + tab + "Number of cards: " + numberOfCardsOnBoard());
        System.out.println("DI" + tab + discardPile.printCard(discardPile.size()-1) + dtab +
                heartsPile.printCard(heartsPile.size() - 1) + tab + spadesPile.printCard(spadesPile.size() - 1)
                + tab + diamondsPile.printCard(diamondsPile.size() - 1) + tab + clubsPile.printCard(clubsPile.size() - 1) +
                tab + "Cards in Draw/Discard: " + (drawPile.size() + discardPile.size()));
        System.out.println("P1  P2  P3  P4  P5  P6  P7");
        for (int i = 0; i < longestNumberPileLength(); i++) {
            System.out.println(pile1.printCard(i) + tab
                    + pile2.printCard(i) + tab
                    + pile3.printCard(i) + tab
                    + pile4.printCard(i) + tab
                    + pile5.printCard(i) + tab
                    + pile6.printCard(i) + tab
                    + pile7.printCard(i));
        }


    }

    /** Author Alec
     * Returns number of cards on board
     */
    public int numberOfCardsOnBoard() {
        return drawPile.size() + discardPile.size() + pile1.size() + pile2.size() + pile3.size() + pile4.size() + pile5.size() + pile6.size() + pile7.size() + heartsPile.size() + spadesPile.size() + diamondsPile.size() + clubsPile.size();
    }

    /** Author STEVEN
     * Returns the length value of the longest number pile, for use in determining the size of the TUI.
     */
    public int longestNumberPileLength() {
        int l = 0;
        ArrayList<CardDeck> piles = new ArrayList<>();
        piles.add(pile1);
        piles.add(pile2);
        piles.add(pile3);
        piles.add(pile4);
        piles.add(pile5);
        piles.add(pile6);
        piles.add(pile7);
        for (CardDeck a : piles) {
            if (a.size() > l) {
                l = a.size();
            }
        }
        return l;
    }

}
