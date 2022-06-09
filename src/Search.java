import java.util.Arrays;
import java.util.List;

public class Search {

    private final Board board;

    public Search(Board board) {
        this.board = board;
    }


    /**
     * Author Aya
     * Searches for a given cardvalue and returns pile number, row number (column index), suit)
     */
    public List<Object> someSearch(int cardValue) {
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






}
