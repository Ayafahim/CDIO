package main;

import java.util.Objects;
import java.util.Scanner;


public class Game {

    /**
     * Author STEVEN
     * Creates a board object that is responsible for the general game loop (taking input/calling the main.AI). Terminates
     * when given the right command or the game is won/lost.
     */
    public void startGame() throws Exception {
        Board board = new Board();
        board.initialPile.populate("cards.txt");
        //board.initialPile.shuffleDeck();
        board.initialPopulateBoard();
        AI ai = new AI(new Search(board), board);

        Scanner sc = new Scanner(System.in);
        String input;
        printCommandOptions();
        do {
            board.updateBoardState();
            board.printBoard(); //comment this out during auto mode
            System.out.println("Ready for Input"); //comment this out during auto mode
            input = sc.nextLine(); //comment this out to turn on auto mode.
            //input = "ai"; //uncomment this to turn on auto mode
            board.parseInput(input);
        } while (!Objects.equals(input, "goodbye"));

    }

    public void startDebug()  throws Exception {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        int input = getInt("Type the number for the debug deck you wish to initialize on the board.");
        board.initialPile.populate("debug" + input + ".txt");
        board.initialPopulateBoard();
        AI ai = new AI(new Search(board), board);

        printCommandOptions();

        String textInput;
        do {
            board.updateBoardState();
            board.printBoard();
            //System.out.println(board.getDeck("draw"));
            //System.out.println(board.getDeck("discard"));
            System.out.println("Ready for Input");
            textInput = sc.nextLine();
            board.parseInput(textInput);
        } while (!Objects.equals(textInput, "goodbye"));
    }

    public static int getInt(String prompt) {
        System.out.println(prompt);
        while(true){
            try {
                return Integer.parseInt(new Scanner(System.in).next());
            } catch (NumberFormatException ne) {
                System.out.println("That's not a valid number. Try again:\n" + prompt);
            }
        }
    }

    public void printCommandOptions() {
        System.out.println("""
                You have the following input options:
                goodbye -> ends program
                shuffle -> shuffles deck
                ai -> calls the ai to make a move for you
                ace -> looks for ace and moves to foundation pile
                deuce -> looks for ace and moves to foundation pile
                restart -> restarts the game using the default cards.txt
                3 numbers separated by spaces to indicate a move with the following syntax:
                source pile : destination pile: card index in source pile
                The numbers representing the piles are 1-7, 8-11 for foundations, 12 for draw, and 13 for discard
                You can use "last" to replace the index number
                --------------------------------------------------------------------------------------------------""");
    }

}

