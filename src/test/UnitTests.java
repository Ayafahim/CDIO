package test;

import main.Board;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Scanner;
import main.*;

import static org.junit.jupiter.api.Assertions.*;
public class UnitTests {

    @Test
    void aceMove() throws Exception {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        board.initialPile.populate("debug" + 1 + ".txt");
        board.initialPopulateBoard();
        AI ai = new AI(new Search(board), board);

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
}
