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

    public void aceMoveToFoundation(){

        System.out.println(search.someCardSearch(1));

        List<Object> aceInfo = search.someCardSearch(1);

         Object srcDeck = aceInfo.get(0);
         CardDeck src = board.getDeck("0");
         CardDeck destination = null;

        switch (aceInfo.get(2).toString()){
            case "Diamonds" -> destination = board.getDeck("10");
            case "Clubs" -> destination = board.getDeck("11");
            case "Spades" -> destination = board.getDeck("9");
            case "Hearts" -> destination = board.getDeck("8");
        }
        switch (srcDeck.toString()){
            case "1" -> src = board.getDeck("1");
            case "2" -> src = board.getDeck("2");
            case "3" -> src = board.getDeck("3");
            case "4" -> src = board.getDeck("4");
            case "5" -> src = board.getDeck("5");
            case "6" -> src = board.getDeck("6");
            case "7" -> src = board.getDeck("7");
        }

        Move move = new Move(src,destination, (Integer) aceInfo.get(1));
        try{
            System.out.println("Move is: " + move);
            board.attemptMove(move);
        }catch (Exception e){
            System.out.println("move couldn't be done");
        }

    }

    public void deuceMoveToFoundation(){

        System.out.println(search.someCardSearch(2));

        List<Object> aceInfo = search.someCardSearch(2);

        Object srcDeck = aceInfo.get(0);
        CardDeck src = board.getDeck("0");
        CardDeck destination = null;

        switch (aceInfo.get(2).toString()){
            case "Diamonds" -> destination = board.getDeck("10");
            case "Clubs" -> destination = board.getDeck("11");
            case "Spades" -> destination = board.getDeck("9");
            case "Hearts" -> destination = board.getDeck("8");
        }
        switch (srcDeck.toString()){
            case "1" -> src = board.getDeck("1");
            case "2" -> src = board.getDeck("2");
            case "3" -> src = board.getDeck("3");
            case "4" -> src = board.getDeck("4");
            case "5" -> src = board.getDeck("5");
            case "6" -> src = board.getDeck("6");
            case "7" -> src = board.getDeck("7");
        }

        Move move = new Move(src,destination, (Integer) aceInfo.get(1));
        try{
            System.out.println("Move is: " + move);
            board.attemptMove(move);
        }catch (Exception e){
            System.out.println("move couldn't be done");
        }

    }


    public void freeDownCardMove(){
        System.out.println(search.searchIfDownCardCanBeFreed());
    }
}
