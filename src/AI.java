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
}
