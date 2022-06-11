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
    // Searches for pile with most facedown cards
    public void mostFacedownSearch() {
        int longest = 0;
        int deckNumber = 0;
        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            if(sourceDeck.getNumberOfFaceDownCards() > longest){
               longest = sourceDeck.getNumberOfFaceDownCards();
               deckNumber = i;
            }
        }
        System.out.println("pile " + deckNumber +" has most facedown cards, with:" + longest + " facedown cards");
    }

    /**
     * Author Alec
     * Searches to see if there is a play to free a downcard, if there are multiple, always free
     * the pile which have the most downcards.
     */

    public List<Object>searchIfDownCardCanBeFreed(){
        boolean found = false;
        int sourceDeckNumber = 0;
        int destinationDeckNumber = 0;

        for (int i = 1; i <= 7 ; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceDeck.canFreeDownCard()){
               CardDeck destinationDeck;

                for (int j = 1; j <= 7 ; j++) {
                    destinationDeck = this.board.getDeck(Integer.toString(j));
                    int dTopCardIndex = destinationDeck.getBottomFaceCardIndex();
                    Card dTopCard = destinationDeck.get(dTopCardIndex);


                if (sourceTopCard.getValue() - dTopCard.getValue() == -1){
                    if (sourceTopCard.isBlack() && dTopCard.isRed() || sourceTopCard.isRed() && dTopCard.isBlack())
                    sourceDeckNumber = i;
                    destinationDeckNumber = j;
                    found = true;

                    }
                }
                if (found){
                    break;
                }
            }
        }
        return Arrays.asList("Source Deck: " + sourceDeckNumber,"Destination Deck: " + destinationDeckNumber);

    }



}
