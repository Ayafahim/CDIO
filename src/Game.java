import java.util.Objects;
import java.util.Scanner;

/** Author STEVEN
 *  Creates a game object that is responsible for the general game loop (taking input/calling the AI). Terminates
 *  when given the right command or the game is won/lost.
 */
public class Game {


    public void startGame() throws Exception {
        Board board = new Board();
        board.initialDeck.populate();
        board.initialDeck.shuffleDeck();
        board.initialPopulateBoard();
        //board.printBoard();

        Scanner sc = new Scanner(System.in);
        String input;
        do {
            board.updateBoardState();
            board.printBoard();
            //System.out.println(board.getDeck("draw"));
            //System.out.println(board.getDeck("discard"));
            System.out.println("Ready for Input");
            input = sc.nextLine();
            board.parseInput(input);
        } while (!Objects.equals(input, "goodbye"));
        /*do {
            board.executeAI();
            if (board.ai.gameIsWon) {
                winGame();
            }
        } while (!board.ai.gameIsLost);*/

    }

    public void winGame() {
        //you won. good job
    }

}
