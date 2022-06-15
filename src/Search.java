import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {

    private final Board board;

    public Search(Board board) {
        this.board = board;
    }


    /**
     * Author Aya
     * Searches for a given cardvalue (faceup in pile 1-7) and returns pile number, row number (column index), suit)
     */
    public List<Object> someCardSearch(int cardValue) {
        int cardValueIndex = 0;
        int deckNumber = 0;
        String cardSuit = "0";


        //for loop iterates through each pile and checks value of each card facing up
        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceTopCard.getValue() == cardValue) {
                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> cardSuit = "Hearts";
                    case SPADES -> cardSuit = "Spades";
                    case DIAMONDS -> cardSuit = "Diamonds";
                    case CLUBS -> cardSuit = "Clubs";
                    default -> cardSuit = "0";
                }
                //if card of certain value isn't facing up deckNumber and cardValueIndex will be 0
                deckNumber = i;
                cardValueIndex = sourceTopCardIndex;
                break;
            }
        }
        return Arrays.asList(deckNumber, cardValueIndex, cardSuit);
    }


    /**
     * Author Aya
     * Searches for pile with most facedown cards
     */

    public void mostFacedownSearch() {
        int longest = 0;
        int deckNumber = 0;
        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            if (sourceDeck.getNumberOfFaceDownCards() > longest) {
                longest = sourceDeck.getNumberOfFaceDownCards();
                deckNumber = i;
            }
        }
        System.out.println("pile " + deckNumber + " has most facedown cards, with:" + longest + " facedown cards");
    }

    /**
     * Author Alec
     * Searches to see if there is a play to free a downcard, if there are multiple, always free
     * the pile which have the most downcards.
     */
    // Algoritmen er ikke helt færdig inu, den kan udvides
    public List<Object> searchIfDownCardCanBeFreed() {
        int cardValueIndex = 0;
        boolean found = false;
        int srcDeckNumber = 0;
        int destnDeckNumber = 0;

        // Search all number piles
        for (int i = 7; i > 0; i--) {

            // Number pile variables for source deck
            CardDeck srcDeck = this.board.getDeck(Integer.toString(i));
            int srcTopCardIndex = srcDeck.getBottomFaceCardIndex();
            Card srcTopCard = srcDeck.get(srcTopCardIndex);

            //Check if moving a card will free a downcard
            if (srcDeck.canFreeDownCard()) {
                CardDeck destnDeck;

                // Search all number piles. Number piles variables for destination deck
                for (int j = 7; j > 0; j--) {
                    destnDeck = this.board.getDeck(Integer.toString(j));
                    int destnTopCardIndex = destnDeck.getBottomFaceCardIndex();
                    Card destnTopCard = destnDeck.get(destnTopCardIndex);

                /*
                An algorithm that sets the value of source deck number and destination deck number
                if top source deck minus top destination deck gives -1, and if the color of those 2 decks is not the same.
                 */
                    if (srcTopCard.getValue() - destnTopCard.getValue() == -1) {
                            if (srcTopCard.isBlack() && destnTopCard.isRed() || srcTopCard.isRed() && destnTopCard.isBlack()) {
                                cardValueIndex = srcTopCardIndex;
                                srcDeckNumber = i;
                                destnDeckNumber = j;
                                found = true;
                            }
                    }
                    if (found) {
                        break;
                    }

                }
            }
        }
        return Arrays.asList(srcDeckNumber, destnDeckNumber, cardValueIndex);
    }
}


