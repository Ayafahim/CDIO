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
        CardDeck destination = null;

        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceTopCard.getValue() == cardValue) {
                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> destination = board.heartsPile;
                    case SPADES -> destination = board.spadesPile;
                    case DIAMONDS -> destination = board.diamondsPile;
                    case CLUBS -> destination = board.clubsPile;
                    default -> destination = board.initialPile;
                }
                deckNumber = i;
                cardValueIndex = sourceTopCardIndex;
                break;
            }
        }
        if (deckNumber == 0 && cardValueIndex == 0) {
            for (int i = 0; i <= this.board.drawPile.size(); i++) {
                CardDeck sourceDeck = this.board.drawPile;
                Card card = sourceDeck.get(i);
                if (card.getValue() == 1) {
                    switch (card.getSuit()) {
                        case HEARTS -> destination = board.heartsPile;
                        case SPADES -> destination = board.spadesPile;
                        case DIAMONDS -> destination = board.diamondsPile;
                        case CLUBS -> destination = board.clubsPile;
                        default -> destination = board.initialPile;
                    }
                    deckNumber = 12;
                    cardValueIndex = i;
                    break;
                }
            }
        }
        return Arrays.asList(deckNumber, cardValueIndex, destination.getName());
    }






}
