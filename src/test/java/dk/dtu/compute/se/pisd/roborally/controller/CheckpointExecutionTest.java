package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckpointExecutionTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        //Initialize board on game controller
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);

        //Initialize player on board
        Player player = new Player(board, null, "player");
        board.addPlayer(player);
        board.setCurrentPlayer(player);

    }

    @Test
    void testV1(){

        Player player = gameController.board.getCurrentPlayer();
        Board board = gameController.board;
        gameController.moveCurrentPlayerToSpace(gameController.board.getSpace(0,0));
        System.out.println("--Test1<The correct path> --");
        System.out.println("Player current pos: x = " + player.getSpace().x + ", y = " + player.getSpace().y);

    //Add checkpoints to spaces
        Space space1 = board.getSpace(1,0); //Checkpoint 1
        Space space2 = board.getSpace(1,1);
        var checkpoint1 = space1.addCheckpoint(1, false);
        var checkpoint2 = space2.addCheckpoint(2, true);//Checkpoint 2

    //Moving to checkpoint 1:
        System.out.println("<Moving player to first checkpoint>");
        gameController.moveCurrentPlayerToSpace(space1);
        System.out.println("player moved to: x = " + player.getSpace().x + " y = " + player.getSpace().y);
        System.out.println("First checkpoint hit: " + checkpoint1.doAction(gameController, space1));

    //Moving to checkpoint 2
        System.out.println("\n<Moving player to second checkpoint>");
        gameController.moveCurrentPlayerToSpace(space2);
        System.out.println("player moved to: x = " + player.getSpace().x + " y = " + player.getSpace().y);
        System.out.println("Second checkpoint hit: " + checkpoint2.doAction(gameController, space2));
    }

    @Test
    void Test2(){
        Player player = gameController.board.getCurrentPlayer();
        Board board = gameController.board;
        gameController.moveCurrentPlayerToSpace(gameController.board.getSpace(0,0));
        System.out.println("\n--Test2 - The wrong path--");
        System.out.println("Player current pos: x = " + player.getSpace().x + ", y = " + player.getSpace().y);

    //Add checkpoints to spaces
        Space space1 = board.getSpace(1,0);
        Space space2 = board.getSpace(1,1);
        var checkpoint1 = space1.addCheckpoint(1, false);
        var checkpoint2 = space2.addCheckpoint(2, true);

    //Moving to checkpoint 2:
        System.out.println("<Moving player to second checkpoint>");
        gameController.moveCurrentPlayerToSpace(space2);
        System.out.println("player moved to: x = " + player.getSpace().x + " y = " + player.getSpace().y);
        System.out.println("Second checkpoint hit: " + checkpoint2.doAction(gameController, space2));

    //Moving to checkpoint 1:

        System.out.println("\n<Moving player to first checkpoint>");
        gameController.moveCurrentPlayerToSpace(space1);
        System.out.println("player moved to: x = " + player.getSpace().x + " y = " + player.getSpace().y);
        System.out.println("First checkpoint hit: " + checkpoint1.doAction(gameController, space1));
    }

}
