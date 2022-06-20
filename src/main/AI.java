package main;

import java.util.*;

public class AI {
    private final Search search;
    private final Board board;

    public ArrayList<Move> movesList = new ArrayList<>();

    public AI(Search search, Board board) {
        this.search = search;
        this.board = board;
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
        Increase priority if king can be moved to empty pile from another pile which allows for freeing a downcard
        IF a card is sitting on another one, do NOT let it move to another card of the same color its sitting on, i.e. a black 4 sitting on a red 5 should
        not move to another red 5 as that is pointless and results in a loop.
        IF a king is already the top card of a stack (not on a downcard), it should never move to another empty pile.
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

    /**
     * Author: Steven
     * The main function driving the AI that calls all other functions when looking for a move chosen by the AI!
     */
    public void think() {

        movesList.clear(); //empty the list first every time!

        aceMoveToFoundation(); //Add any acemoves to the list with priority 90
        deuceMoveToFoundation(); //Add any deucemoves to the list with priority 80
        moveKingIfDeckEmpty(); //Add any kingmoves to the list with priority 70
        moveNumberToNumber(); //Add any generic numbermoves to the list with priority 60
        drawMove(); //Draws cards with priority 10
        freeDownCardMove();

        movesList.sort(Collections.reverseOrder(Comparator.comparingInt(Move::getPriority))); //Sort the available moves by priority
        System.out.println("Sorted moves list:\n" + movesList);
        try {
            System.out.println("Attempting the best move:");
            System.out.println(movesList.get(0));
            board.attemptMove(movesList.get(0));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("movesList was empty :(");
        }

    }

    /**
     * PRIORITY 10 FOR DRAWING CARDS.
     */
    public void drawMove() {
        movesList.add(new Move(board.drawPile, board.discardPile, 0, 10));
    }

    //ToDo Needs to search the top of the discard pile for an ace as well.

    /**
     * Author: Steven
     * Sends any regular discard/number pile to number pile moves to the list with priority 5/6 respectively.
     */
    public void moveNumberToNumber() {
        ArrayList<CardDeck> p = board.numberPiles;
        CardDeck d = board.discardPile;
        for (CardDeck pile : p) { //Separate check that adds any move available from the discard pile with priority 5
            if (d.size() > 0) {
                if (board.canMoveToNumberPile(d, pile, d.getLast())) {
                    movesList.add(new Move(d, pile, d.getLast(), 50));
                }
            }
        }

        for (CardDeck pile : p) { //Loops through number piles (SOURCE)
            if (pile.size() > 0) { //There must be at least 1 card in the pile to be moved. (SOURCE)
                for (int i = 0; i < pile.size(); i++) { //Loop through all cards in the pile (INDEX)
                    if (pile.get(i).isFaceUp()) { //We only bother checking for cards that are actually face-up (SOURCE)
                        for (CardDeck pile2 : p) { //Again looping through piles (DESTINATION)
                            if (board.canMoveToNumberPile(pile, pile2, i)) { //Check if the source card can be moved to this destination (LAST CARD)
                                movesList.add(new Move(pile, pile2, i, 60));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * PRIORITY 9
     */
    public void aceMoveToFoundation() {
        ArrayList<CardDeck> p = board.numberPiles;
        CardDeck d = board.discardPile;
        //Check for discard pile.
        if (d.size() > 0) {
            if (d.get(d.getLast()).getValue() == 1) {
                movesList.add(new Move(d, search.parseFoundation(d.get(d.getLast())), d.getLast(), 90));
            }
        }
        //Check for number piles
        for (CardDeck pile : p) {
            if (pile.size() > 0) {
                if (pile.get(pile.getLast()).getValue() == 1) {
                    movesList.add(new Move(pile, search.parseFoundation(pile.get(pile.getLast())), pile.getLast(), 90));
                }
            }
        }

        /*
        //System.out.println(search.someCardSearch(1));

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
            //.out.println("main.Move is: " + move);
            //board.attemptMove(move);
            movesList.add(move);
        } catch (Exception e) {
            //System.out.println("move couldn't be done");
        }*/

    }

    /**
     * PRIORITY 7
     * NOT FINISHED
     */
    public void moveKingIfDeckEmpty() {
        //TODO
        // Shouldn't be able to clear a spot if there isn't a king to take that cleared spot
        /**
         * Not finished
         * Man skal undersøge hvor mange facedown cards der er i et deck, før man laver trækket fra det deck.
         * Hvis der er 0 facedown kort, så fortsæt heri:
         *      - Forudsætningen af at der er 0 facedown kort, gør at man gerne vil rykke alle kortene herfra over til en anden bunke.
         *      - Man må ikke rykke alle kort fra et deck, med mindre der er en konge der kan tage dets plads.
         *      - Hvis der er en konge, så giv tilladelse til at "clear a spot".
         */
        try {
            List<Object> searchForKing = search.someCardSearch(13);
            Object srcDeck = searchForKing.get(0); //This one must be changed later on, we have to move a king from the BIGGEST pile, not just the first king we find.
            //System.out.println("Printer al information om det deck der er konge i: " + Arrays.toString(searchForKing.toArray()));

            CardDeck src = board.getDeck(srcDeck.toString());
            int index = src.getBottomFaceCardIndex();
            for (int i = 1; i < 8; i++) {
                if (board.getDeck(Integer.toString(i)).size() == 0) {
                    CardDeck dest = board.getDeck(Integer.toString(i));
                    Move move = new Move(src, dest, index, 70);//ToDo FIX PRIORITY
                    try {
                        //System.out.println("main.Move is: " + move);
                        //board.attemptMove(move);
                        movesList.add(move);
                    } catch (Exception e) {
                        //System.out.println("move couldn't be done");
                    }
                } else {//System.out.println("No destination for the king to be put"); //Prints out for all decks, will edit later.
                }
            }
        } catch (Exception e) {
            //System.out.println("No kings available");
        }

    }

    //ToDo Needs to search the top of the discard pile for a deuce as well.

    /**
     * PRIORITY 8
     */
    public void deuceMoveToFoundation() {

        ArrayList<CardDeck> p = board.numberPiles;
        CardDeck d = board.discardPile;
        //Check for discard pile.
        if (d.size() > 0) {
            if (d.get(d.getLast()).getValue() == 2) {
                if (search.parseFoundation(d.get(d.getLast())).size() == 1) {
                    movesList.add(new Move(d, search.parseFoundation(d.get(d.getLast())), d.getLast(), 80));
                }
            }
        }
        //Check for number piles
        for (CardDeck pile : p) {
            if (pile.size() > 0) {
                if (pile.get(pile.getLast()).getValue() == 2) {
                    if (search.parseFoundation(pile.get(pile.getLast())).size() == 1) {
                        movesList.add(new Move(pile, search.parseFoundation(pile.get(pile.getLast())), pile.getLast(), 80));
                    }
                }
            }
        }
        /*
        //System.out.println(search.someCardSearch(2));

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

        Move move = new Move(src, destination, (Integer) aceInfo.get(1),80);//ToDo FIX PRIORITY
        try {
            //System.out.println("main.Move is: " + move);
            //board.attemptMove(move);
            movesList.add(move);
        } catch (Exception e) {
            //System.out.println("move couldn't be done");
        }*/

    }

    /**
     * Author Alec
     * makes a move that frees a downcard if it is possble
     */
    public void freeDownCardMove() {

        ArrayList<CardDeck> sP = board.numberPiles;
        ArrayList<CardDeck> dP = board.numberPiles;

        for (CardDeck sourcePile : sP) {
            if (sourcePile.canFreeDownCard()) {
                for (CardDeck destinationPile : dP) {
                    if (sourcePile.size() > 0 && destinationPile.size() > 0) {
                        if (sourcePile.get(sourcePile.getLast()).getValue() - destinationPile.get(destinationPile.getLast()).getValue() == -1) {
                            if (sourcePile.get(sourcePile.getLast()).isBlack() && destinationPile.get(destinationPile.getLast()).isRed()
                                    || sourcePile.get(sourcePile.getLast()).isRed() && destinationPile.get(destinationPile.getLast()).isBlack()) {
                                movesList.add(new Move(sourcePile, destinationPile, sourcePile.getLast(), 100));
                            }
                        }
                    }
                }
            }
        }
       /* //System.out.println(search.searchIfDownCardCanBeFreed());

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

        Move move = new Move(src, destination, (Integer) openDownCard.get(2),100);//ToDo FIX PRIORITY
        try {
            //System.out.println("main.Move is: " + move);
            //board.attemptMove(move);
            movesList.add(move);
        } catch (Exception e) {
            //System.out.println("move couldn't be done");
        }

        */
    }


    /*
    6. Only play a King that will benefit the column(s) with the biggest pile of downcards,
    unless the play of another King will at least allow a transfer that frees a downcard.

    - Søg efter konger
        - hvis der er flere konger, så tag den konge der er i den pile der er flest kort i, altså med flest downcards
        - med mindre, at ved at man spiller en anden konge, vil tillade en flytning der frigør et downcard

        SNAKKET MED ALEC OG AYA:
        "unless": der er en konge i draw pile, der kan sættes i spil, sådan så at en dronning kan rykkes, og free et downcard.
        dvs. at man ikke altid rykker en konge fra det største pile, da man hellere vil have en konge ud af draw.


     */
    public void moveKingFromBiggestPile() {
        try {
            //have to edit: Biggest pile of DOWNCARDS! not just biggest pile.
            //have to check for the piles face-down cards.
            List<Object> searchForKing = search.someCardSearch(13);

            //Edited the someCardSearch to return all decks with the card in you're searching for.

            //Have to change this code, to allow the player to make the move from, if there is a king available.

            //System.out.println("Printer al information om det deck der er konge i: " + Arrays.toString(searchForKing.toArray()));
            int size = searchForKing.size();

            CardDeck deck = board.getDeck((String) searchForKing.get(0));
            int currentDeckSize1 = deck.size();
            //Skal ændres^^skal tjekke for flest facedown card, og ikke bare size()


            search.mostFacedownSearch();

            for (int i = 0; i < size; i = i + 3) {
                //mangler at ændre
                //if (currentDeckSize1<board.getDeck(searchForKing.get(i).size()){
                //  CardDeck source = board.getDeck((String) searchForKing.get(i));
                //}
            }
            //Nu har vi source fra det deck der har flest kort i sig
/*
            main.CardDeck src = board.getDeck(srcDeck.toString());
            int index = src.getBottomFaceCardIndex();
            for (int i = 1; i<8; i++){
                if (board.getDeck(Integer.toString(i)).size() == 0){
                    main.CardDeck dest = board.getDeck(Integer.toString(i));
                    main.Move move = new main.Move(src, dest, index);
                    try {
                        System.out.println("main.Move is: " + move);
                        //board.attemptMove(move);
                        movesList.add(move);
                    } catch (Exception e) {
                        System.out.println("move couldn't be done");
                    }
                } else System.out.println("No destination for the king to be put"); //Prints out for all decks, will edit later.
            }

 */
        } catch (Exception e) {
            //System.out.println("No kings available");
        }
    }




    /*
    8. Don't play or transfer a 5, 6, 7 or 8 anywhere unless at least one of these situations will apply after the play:

It is smooth with it's next highest even/odd partner in the column
It will allow a play or transfer that will IMMEDIATELY free a downcard
There have not been any other cards already played to the column
You have ABSOLUTELY no other choice to continue playing (this is not a good sign)
     */

}


