package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AI {
    private final Search search;
    private final Board board;

    public ArrayList<Move> movesList = new ArrayList<>();

    public AI(Search search, Board board) {
        this.search = search;
        this.board = board;
    }

    /** Author: Steven
     * The main function driving the AI that calls all other functions when looking for a move chosen by the AI!
     */
    public void think() {

    }

    /*ToDo
        Deck/Discard need to be searchable, with a number of DRAW moves printed in order to obtain the wanted card. (OR JUST DRAW WHEN NO OTHER MOVES ARE AVAILABLE?)
        Unknown cards need to be taken care of, taking input from the image recognition to be given their values.

      ToDo
        Look through move types and add them to the list. Moves must have a weight, given by their priority.
        Moves in order of priority:
        Ace to foundation
        Deuce to foundation
        King to empty spot
        Number to number
        Number from discard to number


      ToDo
        Checks that can change priority:
        Play that frees a downcard (can be recursive) (MAJOR CHECK)
        Moves that free downcards, should always be prioritized in the order of piles with most downcards (MINOR CHECK)
        Moves that free a spot must have a waiting King to occupy the spot. Otherwise drop priority to zero. (MAJOR CHECK)
        Play kings that benefit the column with the most downcards. (MINOR CHECK)
        Moves to foundations that aren't aces or deuces should only be done if: (MAJOR CHECK)
            They do not interfere with next-card protection
            Allow a play/transfer that free a downcard
            Open a space for a same-color card pile transfer that frees a downcard
            Clears a spot for an immediate waiting King
        Don't play/transfer 5-6-7-8 ANYWHERE unless one of these apply:
            It is smooth
            It allows for freeing a downcard
            There have no been any other cards played to the column (only 1 face-up card present)
            You have NO other choices (MAKE DEBUG STATEMENT FOR THIS SINCE IT'S A BAD SIGN)
        If stuck in a position, look to re-arrange stacks or play to ace stacks until you can clear an
        existing pile enough to use an existing card as substitute for the necessary card (DIFFICULT LAST-PRIORITY FEATURE)


    */
    //ToDo Needs to search the top of the discard pile for an ace as well.
    public void aceMoveToFoundation() {

        System.out.println(search.someCardSearch(1));

        List<Object> aceInfo = search.someCardSearch(1);

        Object srcDeck = aceInfo.get(0);
        CardDeck src = board.getDeck("0");
        CardDeck destination = null;

        switch (aceInfo.get(2).toString()) {
            case "Diamonds" -> destination = board.getDeck("10");
            case "Clubs" -> destination = board.getDeck("11");
            case "Spades" -> destination = board.getDeck("9");
            case "Hearts" -> destination = board.getDeck("8");
        }
        switch (srcDeck.toString()) {
            case "1" -> src = board.getDeck("1");
            case "2" -> src = board.getDeck("2");
            case "3" -> src = board.getDeck("3");
            case "4" -> src = board.getDeck("4");
            case "5" -> src = board.getDeck("5");
            case "6" -> src = board.getDeck("6");
            case "7" -> src = board.getDeck("7");
        }

        Move move = new Move(src, destination, (Integer) aceInfo.get(1),9);//ToDo FIX PRIORITY
        try {
            System.out.println("main.Move is: " + move);
            board.attemptMove(move);
        } catch (Exception e) {
            System.out.println("move couldn't be done");
        }

    }

    /**
     * Not finished
     */
    public void moveKingIfDeckEmpty() {
        //TODO
        // Shouldn't be able to clear a spot if there isn't a king to take that cleared spot
        /**
         * Not finished
         */
        try {
            List<Object> searchForKing = search.someCardSearch(13);
            Object srcDeck = searchForKing.get(0); //This one must be changed later on, we have to move a king from the BIGGEST pile, not just the first king we find.
            System.out.println("Printer al information om det deck der er konge i: " + Arrays.toString(searchForKing.toArray()));

            CardDeck src = board.getDeck(srcDeck.toString());
            int index = src.getBottomFaceCardIndex();
            for (int i = 1; i<8; i++){
                if (board.getDeck(Integer.toString(i)).size() == 0){
                    CardDeck dest = board.getDeck(Integer.toString(i));
                    Move move = new Move(src, dest, index,7);//ToDo FIX PRIORITY
                    try {
                        System.out.println("main.Move is: " + move);
                        board.attemptMove(move);
                    } catch (Exception e) {
                        System.out.println("move couldn't be done");
                    }
                } else System.out.println("No destination for the king to be put"); //Prints out for all decks, will edit later.
            }
        } catch (Exception e) {
            System.out.println("No kings available");
        }

    }

    //ToDo Needs to search the top of the discard pile for a deuce as well.
    public void deuceMoveToFoundation() {

        System.out.println(search.someCardSearch(2));

        List<Object> aceInfo = search.someCardSearch(2);

        Object srcDeck = aceInfo.get(0);
        CardDeck src = board.getDeck("0");
        CardDeck destination = null;

        switch (aceInfo.get(2).toString()) {
            case "Diamonds" -> destination = board.getDeck("10");
            case "Clubs" -> destination = board.getDeck("11");
            case "Spades" -> destination = board.getDeck("9");
            case "Hearts" -> destination = board.getDeck("8");
        }
        switch (srcDeck.toString()) {
            case "1" -> src = board.getDeck("1");
            case "2" -> src = board.getDeck("2");
            case "3" -> src = board.getDeck("3");
            case "4" -> src = board.getDeck("4");
            case "5" -> src = board.getDeck("5");
            case "6" -> src = board.getDeck("6");
            case "7" -> src = board.getDeck("7");
        }

        Move move = new Move(src, destination, (Integer) aceInfo.get(1),8);//ToDo FIX PRIORITY
        try {
            System.out.println("main.Move is: " + move);
            board.attemptMove(move);
        } catch (Exception e) {
            System.out.println("move couldn't be done");
        }

    }

    /**
     * Author Alec
     * makes a move that frees a downcard if it is possble
     */
    public void freeDownCardMove() {
        //System.out.println(search.searchIfDownCardCanBeFreed());

        List<Object> openDownCard = search.searchIfDownCardCanBeFreed();
        Object srcDeck = openDownCard.get(0);
        CardDeck src = board.getDeck("0");
        CardDeck destination = null;

        switch (srcDeck.toString()) {
            case "1" -> src = board.getDeck("1");
            case "2" -> src = board.getDeck("2");
            case "3" -> src = board.getDeck("3");
            case "4" -> src = board.getDeck("4");
            case "5" -> src = board.getDeck("5");
            case "6" -> src = board.getDeck("6");
            case "7" -> src = board.getDeck("7");
        }

        switch (openDownCard.get(1).toString()) {
            case "1" -> destination = board.getDeck("1");
            case "2" -> destination = board.getDeck("2");
            case "3" -> destination = board.getDeck("3");
            case "4" -> destination = board.getDeck("4");
            case "5" -> destination = board.getDeck("5");
            case "6" -> destination = board.getDeck("6");
            case "7" -> destination = board.getDeck("7");
        }

        Move move = new Move(src, destination, (Integer) openDownCard.get(2),10);//ToDo FIX PRIORITY
        try {
            System.out.println("main.Move is: " + move);
            board.attemptMove(move);
        } catch (Exception e) {
            System.out.println("move couldn't be done");
        }
    }



    public void moveKingFromBiggestPile() {
        try {
            List<Object> searchForKing = search.someCardSearch(13);

            //Edited the someCardSearch to return all decks with the card in you're searching for.

            System.out.println("Printer al information om det deck der er konge i: " + Arrays.toString(searchForKing.toArray()));
            int size = searchForKing.size();
            CardDeck currentDeck = board.getDeck("7");
            for (int i = 0; i<size; i=i+3){
                if (board.getDeck(Integer.toString(i)).size()>currentDeck.size() ){
                    CardDeck source = board.getDeck((String) searchForKing.get(i));
                    System.out.println(source);
                } else System.out.println(currentDeck);
            }
/*
            main.CardDeck src = board.getDeck(srcDeck.toString());
            int index = src.getBottomFaceCardIndex();
            for (int i = 1; i<8; i++){
                if (board.getDeck(Integer.toString(i)).size() == 0){
                    main.CardDeck dest = board.getDeck(Integer.toString(i));
                    main.Move move = new main.Move(src, dest, index);
                    try {
                        System.out.println("main.Move is: " + move);
                        board.attemptMove(move);
                    } catch (Exception e) {
                        System.out.println("move couldn't be done");
                    }
                } else System.out.println("No destination for the king to be put"); //Prints out for all decks, will edit later.
            }

 */
        } catch (Exception e) {
            System.out.println("No kings available");
        }
    }
}

    /*
    6. Only play a King that will benefit the column(s) with the biggest pile of downcards,
    unless the play of another King will at least allow a transfer that frees a downcard.

    - Søg efter konger
        - hvis der er flere konger, så tag den konge der er i den pile der er flest kort i, altså med flest downcards
        - med mindre, at ved at man spiller en anden konge, vil tillade en flytning der frigør et downcard


     */

