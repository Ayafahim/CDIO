import java.util.Arrays;
import java.util.List;

public class AI {

    private final Search search;
    private final Board board;

    public AI(Search search, Board board) {
        this.search = search;
        this.board = board;
    }

    public void doShit() {
        System.out.println(search.toString());
        System.out.println(board.toString());

    }

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

        Move move = new Move(src, destination, (Integer) aceInfo.get(1));
        try {
            System.out.println("Move is: " + move);
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
                    Move move = new Move(src, dest, index);
                    try {
                        System.out.println("Move is: " + move);
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

        Move move = new Move(src, destination, (Integer) aceInfo.get(1));
        try {
            System.out.println("Move is: " + move);
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

        Move move = new Move(src, destination, (Integer) openDownCard.get(2));
        try {
            System.out.println("Move is: " + move);
            board.attemptMove(move);
        } catch (Exception e) {
            System.out.println("move couldn't be done");
        }
    }



    public void moveKingFromBiggestPile() {
        try {
            List<Object> searchForKing = search.someCardSearch(13);

            //Edited the someCardSearch to return all decks with the card in you're searching for.

            //Have to change this code, to allow the player to make the move from, if there is a king available.

            System.out.println("Printer al information om det deck der er konge i: " + Arrays.toString(searchForKing.toArray()));
            int size = searchForKing.size();
            Object currentDeckSize = searchForKing.get(1);
            int currentDeckSize1 = (int) currentDeckSize;

            for (int i = 1; i<size; i=i+4){
                if (currentDeckSize1<board.getDeck(Integer.toString(i)).size()){
                    CardDeck source = board.getDeck((String) searchForKing.get(i));
                } else System.out.println(currentDeckSize);
            }
            //Nu har vi source fra det deck der har flest kort i sig
/*
            CardDeck src = board.getDeck(srcDeck.toString());
            int index = src.getBottomFaceCardIndex();
            for (int i = 1; i<8; i++){
                if (board.getDeck(Integer.toString(i)).size() == 0){
                    CardDeck dest = board.getDeck(Integer.toString(i));
                    Move move = new Move(src, dest, index);
                    try {
                        System.out.println("Move is: " + move);
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


    /*
    7. Only build your Ace stacks (with anything other than an Ace or Deuce) when the play will:

Not interfere with your Next Card Protection
Allow a play or transfer that frees (or allows a play that frees) a downcard
Open up a space for a same-color card pile transfer that allows a downcard to be freed
Clear a spot for an IMMEDIATE waiting King (it cannot be to simply clear a spot)

     */

    public void whenToPlayAce() {

    }







    /*
    8. Don't play or transfer a 5, 6, 7 or 8 anywhere unless at least one of these situations will apply after the play:

It is smooth with it's next highest even/odd partner in the column
It will allow a play or transfer that will IMMEDIATELY free a downcard
There have not been any other cards already played to the column
You have ABSOLUTELY no other choice to continue playing (this is not a good sign)
     */
}

    /*
    6. Only play a King that will benefit the column(s) with the biggest pile of downcards,
    unless the play of another King will at least allow a transfer that frees a downcard.

    - Søg efter konger
        - hvis der er flere konger, så tag den konge der er i den pile der er flest kort i, altså med flest downcards
        - med mindre, at ved at man spiller en anden konge, vil tillade en flytning der frigør et downcard


     */



