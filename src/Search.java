import java.util.Arrays;
import java.util.List;

public class Search {

    private final Board board;
    int aceIndex = 0;

    public int getAceIndex() {
        return aceIndex;
    }

    public void setAceIndex(int aceIndex) {
        this.aceIndex = aceIndex;
    }


    public int getDeckNumber() {
        return deckNumber;
    }

    public void setDeckNumber(int deckNumber) {
        this.deckNumber = deckNumber;
    }

    public CardDeck getDestination() {
        return destination;
    }

    public void setDestination(CardDeck destination) {
        this.destination = destination;
    }

    int deckNumber = 0;
    CardDeck destination = null;

    public Search(Board board) {
        this.board = board;
    }

    public void doShit() {
        System.out.println(board.toString());
    }

    /** Author Aya
     Searches for revealed aces and returns pile number, row number (bottom = 1), suit)
     */

    public List<Object> aceSearch() {

        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceTopCard.getValue() == 1) {
                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> destination = board.heartsPile;
                    case SPADES -> destination = board.spadesPile;
                    case DIAMONDS -> destination = board.diamondsPile;
                    case CLUBS -> destination = board.clubsPile;
                    default -> destination = board.initialDeck;
                }
                setDeckNumber(i);
                setAceIndex(sourceTopCard.getValue());
                break;
            }
        }
        if (deckNumber == 0 && aceIndex == 0) {
            for (int i = 0; i <= this.board.drawDeck.size(); i++) {
                CardDeck sourceDeck = this.board.drawDeck;
                Card card = sourceDeck.get(i);
                if (card.getValue() == 1) {
                    switch (card.getSuit()) {
                        case HEARTS -> destination = board.heartsPile;
                        case SPADES -> destination = board.spadesPile;
                        case DIAMONDS -> destination = board.diamondsPile;
                        case CLUBS -> destination = board.clubsPile;
                        default -> destination = board.initialDeck;
                    }
                    setDeckNumber(12);
                    setAceIndex(i);
                    break;
                }
            }

        }
        return Arrays.asList(deckNumber, aceIndex, destination.getName());
    }

    /** Author Aya
     Searches for revealed deuce and returns pile number, row number (bottom = 1), suit)
     */
    public List<Object> deuceSearch() {

        for (int i = 1; i <= 7; i++) {
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceTopCard.getValue() == 2) {
                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> destination = board.heartsPile;
                    case SPADES -> destination = board.spadesPile;
                    case DIAMONDS -> destination = board.diamondsPile;
                    case CLUBS -> destination = board.clubsPile;
                    default -> destination = board.initialDeck;
                }
                setDeckNumber(i);
                setAceIndex(sourceTopCard.getValue());
                break;
            }
        }
        if (deckNumber == 0 && aceIndex == 0) {
            for (int i = 0; i <= this.board.drawDeck.size(); i++) {
                CardDeck sourceDeck = this.board.drawDeck;
                Card card = sourceDeck.get(i);
                if (card.getValue() == 1) {
                    switch (card.getSuit()) {
                        case HEARTS -> destination = board.heartsPile;
                        case SPADES -> destination = board.spadesPile;
                        case DIAMONDS -> destination = board.diamondsPile;
                        case CLUBS -> destination = board.clubsPile;
                        default -> destination = board.initialDeck;
                    }
                    setDeckNumber(12);
                    setAceIndex(i);
                    break;
                }
            }

        }
        return Arrays.asList(deckNumber, aceIndex, destination.getName());
    }
}
