package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Checkpoint_findLastCheckpointTest {
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setup(){
        //Initialize board on game controller
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);

        //Initialize player on board
        Player player = new Player(board, null, "player");
        board.addPlayer(player);
        board.setCurrentPlayer(player);
    }

    @Test
    void Test1(){
        System.out.print("--Last checkpoint method <test1>--\n");
        System.out.print("--Should return 3--\n");
        Checkpoint checkpoint1 = gameController.board.getSpace(0,0).addCheckpoint(3,false);
        Checkpoint checkpoint2 = gameController.board.getSpace(0,1).addCheckpoint(2,false);
        Checkpoint checkpoint3 = gameController.board.getSpace(0,2).addCheckpoint(1,false);

        System.out.println("Checkpoint 1, x = 0, y = 0, no. = " + checkpoint1.getCheckPointNumber());
        System.out.println("Checkpoint 2, x = 0, y = 1, no. = " + checkpoint2.getCheckPointNumber());
        System.out.println("Checkpoint 3, x = 0, y = 2, no. = " + checkpoint3.getCheckPointNumber());

        System.out.println("Last checkpoint space: " + gameController.getLastCheckpointSpace().x + " " + gameController.getLastCheckpointSpace().y);
    }

    @Test
    void Test2(){
        System.out.print("--Last checkpoint method <test2>--\n");
        System.out.print("--Should return 5--\n");
        Checkpoint checkpoint1 = gameController.board.getSpace(0,0).addCheckpoint(3,false);
        Checkpoint checkpoint2 = gameController.board.getSpace(0,1).addCheckpoint(4,false);
        Checkpoint checkpoint3 = gameController.board.getSpace(0,2).addCheckpoint(1,false);
        Checkpoint checkpoint4 = gameController.board.getSpace(0,3).addCheckpoint(2,false);
        Checkpoint checkpoint5 = gameController.board.getSpace(0,4).addCheckpoint(5,false);

        System.out.println("Checkpoint 1, x = 0, y = 0, no. = " + checkpoint1.getCheckPointNumber());
        System.out.println("Checkpoint 2, x = 0, y = 1, no. = " + checkpoint2.getCheckPointNumber());
        System.out.println("Checkpoint 3, x = 0, y = 2, no. = " + checkpoint3.getCheckPointNumber());
        System.out.println("Checkpoint 4, x = 0, y = 3, no. = " + checkpoint4.getCheckPointNumber());
        System.out.println("Checkpoint 5, x = 0, y = 4, no. = " + checkpoint5.getCheckPointNumber());

        System.out.println("Last checkpoint space: " + gameController.getLastCheckpointSpace().x + " " + gameController.getLastCheckpointSpace().y);
    }

}
