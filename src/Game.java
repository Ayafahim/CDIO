import java.util.Objects;
import java.util.Scanner;

/**
 * Author STEVEN
 * Creates a game object that is responsible for the general game loop (taking input/calling the AI). Terminates
 * when given the right command or the game is won/lost.
 */
public class Game {


    public void startGame() throws Exception {
        Board board = new Board();
        board.initialPile.populate("cards.txt");
        board.initialPile.shuffleDeck();
        board.initialPopulateBoard();
        AI ai = new AI(new Search(board), board);

        Scanner sc = new Scanner(System.in);
        String input;
        printCommandOptions();
        do {
            board.updateBoardState();
            board.printBoard();
            ai.aceMove();
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

    public void startDebug()  throws Exception {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        int input = -1;
        do {
            System.out.println("type the number for the debug deck you wish to initialize on the board.");
            if (sc.hasNextInt()) {
                input = sc.nextInt();
                break;
            }
        } while (!sc.hasNextInt());

        board.initialPile.populate("debug" + input + ".txt");
        board.initialPopulateBoard();
        AI ai = new AI(new Search(board), board);

        printCommandOptions();

        String textInput;
        do {
            board.updateBoardState();
            board.printBoard();
            ai.aceMove();
            //System.out.println(board.getDeck("draw"));
            //System.out.println(board.getDeck("discard"));
            System.out.println("Ready for Input");
            textInput = sc.nextLine();
            board.parseInput(textInput);
        } while (!Objects.equals(textInput, "goodbye"));
    }

    public void winGame() {
        //you won. good job
    }

    public void printCommandOptions() {
        System.out.println("You have the following input options:\n" +
                "goodbye -> ends program\n" +
                "shuffle -> shuffles deck\n" +
                "ai -> calls the ai to make a move for you\n" +
                "restart -> restarts the game using the default cards.txt\n" +
                "3 numbers separated by spaces to indicate a move with the following syntax:\n" +
                "source pile : destination pile: card index in source pile\n" +
                "The numbers representing the piles are 1-7, 8-11 for foundations, 12 for draw, and 13 for discard\n" +
                "You can use \"last\" to replace the index number\n" +
                "--------------------------------------------------------------------------------------------------");
    }

}

