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


        Checkpoint checkpoint1 = gameController.board.getSpace(0,0).addCheckpoint(1,false);
        Checkpoint checkpoint2 = gameController.board.getSpace(0,1).addCheckpoint(2,false);
        Checkpoint checkpoint3 = gameController.board.getSpace(0,2).addCheckpoint(3,false);
        System.out.println("Last checkpoint: " + gameController.board.getLastCheckpoint(gameController.board));
    }

}
