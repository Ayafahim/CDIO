package main;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class AI {
    private final Search search;
    private final Board board;

    public boolean panicMode = false;

    public ArrayList<Move> movesList = new ArrayList<>();

    public ArrayList<Move> memory = new ArrayList<>(20);

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

        FIXED:
        1. IF a card is sitting on another one, do NOT let it move to another card of the same color its sitting on, i.e. a black 4 sitting on a red 5 should
        not move to another red 5 as that is pointless and results in a loop.

        The next 3 are fixed since the freeDowncardMove will prioritize all moves possible with most downcards or any at all:
        2. Moves that free downcards, should always be prioritized in the order of piles with most downcards (MINOR CHECK)
        3. Increase priority if king can be moved to empty pile from another pile which allows for freeing a downcard
        4. Play that frees a downcard (can be recursive) (MAJOR CHECK)

      ToDo
        Checks that can change priority:
        IF a king is already the top card of a stack (not on a downcard), it should never move to another empty pile.
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
     * Uncomment line 121 (panicMode = false) for more correct behavior that takes up a lot of moves to do.
     * It can be used by turning off panic mode manually when wanted using the text command.
     */
    public void think() {

        movesList.clear(); //empty the list first every time!

        aceMoveToFoundation(); //Add any acemoves to the list with priority 90
        deuceMoveToFoundation(); //Add any deucemoves to the list with priority 80
        //moveKingIfDeckEmpty(); //Add any kingmoves to the list with priority 70
        moveNumberToNumber(); //Add any generic numbermoves to the list with priority 60
        drawMove(); //Draws cards with priority 20
        moveToFoundation(); //Adds moves to foundations with priority 10

        if (findRepeatStateInMemory()) {
            panicMode = true;
            //System.out.println("REPEAT BOARD STATE DETECTED!");
            //printPanicState();
        }

        if (board.discardPile.size() + board.drawPile.size() == 3 && board.discardPile.size() != 3) {
            System.out.println("DRAW/DISCARD only have 3 cards. Flipping them immediately to avoid being stuck.");
            try {
                movesList.add(new Move(board.drawPile, board.discardPile, 0, 10000000));
            } catch (Exception e) {
                System.out.println("Error wtf?");
            }
        }

        performChecks(); //Performs checks that alter priority before execution.



        movesList.sort(Collections.reverseOrder(Comparator.comparingInt(Move::getPriority))); //Sort the available moves by priority
        //System.out.println("Sorted moves list:\n" + movesList);
        try {
            //System.out.println("Attempting the best move:");
            if (movesList.size() == 0) {
                boolean won = true;
                for (CardDeck pile: board.searchPiles) {
                    if (pile.size() != 0) {
                        won = false;
                        break;
                    }
                }
                if (won) {
                    gameWon(); //You win! :D
                    System.exit(0);
                }
            }
            System.out.println(movesList.get(0));
            board.attemptMove(movesList.get(0));
            if (!board.isFoundationPile(movesList.get(0).getDestinationDeck())) { //If the executed move was NOT to a foundation pile, disengage panic mode.
                if (panicMode) {
                    //System.out.println("NON-Draw move was executed, so there is no need to panic!");
                    //panicMode = false;
                    //printPanicState();
                }
            }
            addToMemory(movesList.get(0));
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            System.out.println("movesList was empty :(");

        }

    }

    /** AUTHOR Steven
     * Loops through each move and performs all the checks on each of them, altering their priorities.
     */
    public void performChecks() {
        for (Move move: movesList) {
            freeDownCardCheck(move); // add number of downcards to priority for each move in moveslist
            numberMoveIsKingCheck(move); //Adds priority to a numberToNumber move if a king is being moved to an empty spot.
            panicCheck(move); //Adds 20 priority to foundation moves while panic mode is engaged, bringing their prio to 30, higher than drawing more cards.
        }
    }

    public void panicCheck(Move move) {
        if (panicMode) {
            if (board.isFoundationPile(move.getDestinationDeck())) {
                move.setPriority(move.getPriority() + 20);
            }
        }
    }

    public void numberMoveIsKingCheck(Move move) {
        /*  Source must be a number pile
            Move must be a King
            The index must be 0

            If these 3 conditions apply, the move is useless.
         */
        if (board.isNumberPile(move.getSourceDeck()) && move.getSourceDeck().get(move.getIndex()).getValue() == 13 && move.getIndex() == 0) {
            move.setPriority(0);
        }
        /*
            Destination must be a number pile
            Destination deck must be empty.
            Card being moved must be a king (This should always be true)

         */
        else if (move.getDestinationDeck().size() == 0 && move.getSourceDeck().get(move.getIndex()).getValue() == 13 && board.isNumberPile(move.getDestinationDeck())) {
            move.setPriority(move.getPriority() + 10); //This move should increase from 60 to 70, generally, putting it only behind ace and deuce moves.
        }
    }


    /** Author Aya
     * Adds moves where cards can be put on the foundations. Does not check for aces and deuces. PRIORITY 10
     */
    public void moveToFoundation() {
        for (CardDeck pile:board.searchPiles) {
            //Matches the number of cards in the foundation with the value of the card. The card must be 1 larger, i.e. a 5 of hearts can be placed on a hearts foundation of size 4.
            if (pile.size() > 0) {
                if (pile.get(pile.getLast()).getValue() - board.getSuitPile(pile.get(pile.getLast()).getSuit()).size() == 1) {
                    movesList.add(new Move(pile, board.getSuitPile(pile.get(pile.getLast()).getSuit()), pile.getLast(), 10));
                }
            }
        }
    }

    /** Author STEVEN
     * @return A boolean of whether a repeat state was found.
     */
    public boolean findRepeatStateInMemory() {

        if (memory.size() < 4) { //Logic isn't solid, but will not activate panic mode during the start of the game.
            return false;
        }
        for (Move savedState: memory) {
            if (!(savedState.getDestinationDeck() == board.discardPile)) { //If any moves are non-DRAW, return false
                return false;
            }
        }

        return true;
    }

    public void printPanicState() {
        if (panicMode) {
            System.out.println("PANIC MODE ENGAGED!!! :O");
        }
        else {
            System.out.println("PANIC MODE disengaged.... wooh =)");
        }
    }

    /**AUTHOR Steven
     * Method to add a boardstate to the memory.
     */
    public void addToMemory(Move move) {
        memory.add(move);
        if (memory.size() > (board.discardPile.size() + board.drawPile.size()) + 3) {
            memory.remove(0); //The whole draw pile should be passed through
        }
    }

    /**
     * PRIORITY 20 FOR DRAWING CARDS.
     */
    public void drawMove() {
        if (board.drawPile.size() + board.discardPile.size() > 3) {
            movesList.add(new Move(board.drawPile, board.discardPile, 0, 20));
        }
    }

    /**
     * Author: Steven
     * Sends any regular discard/number pile to number pile moves to the list with priority 59/60 respectively.
     */
    public void moveNumberToNumber() {
        ArrayList<CardDeck> p = board.numberPiles;
        CardDeck d = board.discardPile;
        for (CardDeck pile : p) { //Separate check that adds any move available from the discard pile with priority 5
            if (d.size() > 0) {
                if (board.canMoveToNumberPile(d, pile, d.getLast())) { //Checks if move is possible
                        movesList.add(new Move(d, pile, d.getLast(), 59));
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

    /** Author Aya
     * PRIORITY 90
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

    }

    /**
     * Author Jacob
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
                        System.out.println("move couldn't be done");
                    }
                } else {//System.out.println("No destination for the king to be put"); //Prints out for all decks, will edit later.
                    movesList.add(new Move(board.drawPile, board.discardPile, 0, 10));
                }
            }
        } catch (Exception e) {
            System.out.println("No kings available");
        }

    }

    /** Author Aya
     * 9: moves given card to foundation pile
     * ToDo FIX PRIORITY
     */
    public  void buildFoundationStack(int cardValue){

        ArrayList<CardDeck> p = board.numberPiles;
        CardDeck d = board.discardPile;
        //Check for discard pile.
        if (d.size() > 0) {
            if (d.get(d.getLast()).getValue() == cardValue) {
                movesList.add(new Move(d, search.parseFoundation(d.get(d.getLast())), d.getLast(), 1));
            }
        }
        //Check for number piles
        for (CardDeck pile : p) {
            if (pile.size() > 0) {
                if (pile.get(pile.getLast()).getValue() == cardValue) {
                    movesList.add(new Move(pile, search.parseFoundation(pile.get(pile.getLast())), pile.getLast(), 1));
                }
            }
        }
    }

    public void ifStuck(){
        int amountOfDraws = board.discardPile.size() / 3;
        if ((board.discardPile.size() % 3) == 0){


        }
    }


    /**
     * Author Aya
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
     * Edit Aya, to fit new structure: goes through moves list and changes priority for the moves if they can free a downcard by adding number of downcards
     * to priority ex. move can free 6 downcards,  priority += 6
     */
    public void freeDownCardCheck(Move move) {

                if (move.getSourceDeck() != board.drawPile) {
                    int numberOfFaceDownCards = move.getSourceDeck().getNumberOfFaceDownCards();
                    move.setPriority(move.getPriority() + numberOfFaceDownCards);
                }

/*
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
        }*/

    }

    /**
     * Author Jacob
     * 6. Only play a King that will benefit the column(s) with the biggest pile of downcards,
     * unless the play of another King will at least allow a transfer that frees a downcard.
     */
    public void moveKingFromBiggestPile() {
        try {

            List<Object> searchForKing = search.someCardSearch(13);

            //Have to change this code, to allow the player to make the move from, if there is a king available.

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

    /**
     * Authors Jacob & Alec
     *  8. Don't play or transfer a 5, 6, 7 or 8 anywhere unless at least one of these situations will apply after the play:
     * It is smooth with it's next highest even/odd partner in the column
     * It will allow a play or transfer that will IMMEDIATELY free a downcard
     * There have not been any other cards already played to the column
     * You have ABSOLUTELY no other choice to continue playing (this is not a good sign)
     */
    public void dontMoveUnless() {
        try {
            ArrayList<CardDeck> source = board.numberPiles;
            for (CardDeck sourcePile : source) {
                int srcTopCardIndex = sourcePile.getBottomFaceCardIndex();
                Card srcTopCard = sourcePile.get(srcTopCardIndex);

                if (srcTopCard.getValue() == 5 || srcTopCard.getValue() == 6 ||
                        srcTopCard.getValue() == 7 || srcTopCard.getValue() == 8) {
                    ArrayList<CardDeck> destination = board.numberPiles;
                    for (CardDeck destinationPile : destination) {


                        if (sourcePile.get(sourcePile.getLast()).getValue() - destinationPile.get(destinationPile.getLast()).getValue() == -1 &&
                                destinationPile.get(destinationPile.getLast()).isBlack() && sourcePile.get(sourcePile.getLast()).isRed() ||
                                sourcePile.get(sourcePile.getLast()).getValue() - destinationPile.get(destinationPile.getLast()).getValue() == -1 &&
                                        destinationPile.get(destinationPile.getLast()).isRed() && sourcePile.get(sourcePile.getLast()).isBlack()) {

                            if (destinationPile.get(destinationPile.getSecondLast()).isFaceUp()) {
                                if (destinationPile.get(destinationPile.getSecondLast()).getSuit() == sourcePile.get(sourcePile.getLast()).getSuit()) {
                                    movesList.add(new Move(sourcePile, destinationPile, sourcePile.getLast(), 120));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Move couldn't be made");
        }
    }

    public void gameWon() {
        board.printBoard();
        for (int i = 0; i < 10; i++) {
            System.out.println("WINNER WINNER CHICKEN DINNER NICE JOB CONGRATS WOHOO EASY PEASY! :)");
        }
    }

}


