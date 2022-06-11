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

    public void aceMove(){
        //System.out.println(search.someCardSearch(1));
        search.mostFacedownSearch();
        /*
        System.out.println(search.aceSearch());
        CardDeck src = board.getDeck(Integer.toString(search.getDeckNumber()));
        CardDeck destination = null;

        switch ( search.getDestination().getName()){
            case "Diamonds Foundation" -> destination = board.getDeck("10");
            case "Clubs Foundation" -> destination = board.getDeck("11");
            case "Spades Foundation" -> destination = board.getDeck("9");
            case "Hearts Foundation" -> destination = board.getDeck("8");
        }
        Move move = new Move(src,destination,search.getAceIndex());
         */
    }
}
