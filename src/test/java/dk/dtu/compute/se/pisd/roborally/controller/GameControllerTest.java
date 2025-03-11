package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Test for Assignment V1 (can be deleted later once V1 was shown to the teacher)
     */
    @Test
    void testV1() {
        Board board = gameController.board;

        Player player = board.getCurrentPlayer();
        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should be on Space (0,4)!");
    }


//     The following tests should be used later for assignment V2

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }
    @Test
    void moveForward_WithWallInSpace() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space space = current.getSpace();
        space.addWall(current.getHeading());
        gameController.moveForward(current);

        Assertions.assertEquals(current, space.getPlayer());
        Space forwardSpace = board.getNeighbour(space,current.getHeading());
           Assertions.assertTrue(forwardSpace ==null|| forwardSpace.getPlayer()==null,"Player can not move forward because of wall");
    }
    @Test
    void moveForward_WithWallInNeighbourSpace() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space space = current.getSpace();
        Space forwardSpace = board.getNeighbour(space,current.getHeading());
        forwardSpace.addWall(current.getHeading().next().next());
        gameController.moveForward(current);

        Assertions.assertEquals(current, space.getPlayer());
        Assertions.assertTrue(forwardSpace ==null|| forwardSpace.getPlayer()==null,"Player can not move forward because of wall");
    }
    @Test
    void moveForward_WithAnotherPlayerInNeighbourSpace() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space space = current.getSpace();
        Space forwardSpace = board.getNeighbour(space,current.getHeading());

        Assertions.assertNotNull(forwardSpace,"Forward space should exist!");
        if (forwardSpace.getPlayer() == null) {
            Player other = new Player(board, "","Other");
            other.setSpace(forwardSpace);
        }
        Player expectOther = forwardSpace.getPlayer();
        Assertions.assertNotNull(expectOther,"There should be another player in the forward space!");
        Space nextSpace = board.getNeighbour(forwardSpace,current.getHeading());

        boolean canOtherMove = (nextSpace != null && nextSpace.getPlayer() == null);
        gameController.moveForward(current);
        Assertions.assertEquals(current,forwardSpace.getPlayer(),"Current player should move to the forward space");

        if(canOtherMove){
            Assertions.assertEquals(expectOther,nextSpace.getPlayer(),"Other player should be pushed to the next space");
        }else{
           Assertions.assertEquals(expectOther,forwardSpace.getPlayer(),"Other player should remain in forward space if push is not possible!");
        }
        Assertions.assertNull(space.getPlayer(),"space should be empty");
    }

}