import java.util.Arrays;
import java.util.List;

public class Search {

    private final Board board;

    public Search(Board board) {
        this.board = board;
    }

    public void doShit() {
        System.out.println(board.toString());
    }

    public List<Object> aceSearch (){

        int aceIndex = 0;
        for (int i = 1; i <=7 ; i++) {

            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            if (sourceTopCard.getValue()== 1){
                aceIndex = sourceTopCardIndex;

            }


        }
        return Arrays.asList(sourceDeck, aceIndex);

    }
}
