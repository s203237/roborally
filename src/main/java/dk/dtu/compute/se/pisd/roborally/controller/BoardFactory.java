package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;

import java.util.Arrays;
import java.util.List;

/**
 * A factory for creating boards. The factory itself is implemented as a singleton.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// XXX A3: might be used for creating a first slightly more interesting board.
public class BoardFactory {

    /**
     * The single instance of this class, which is lazily instantiated on demand.
     */
    static private BoardFactory instance = null;

    /**
     * Constructor for BoardFactory. It is private in order to make the factory a singleton.
     */
    private BoardFactory() {
    }

    /**
     * Returns the single instance of this factory. The instance is lazily
     * instantiated when requested for the first time.
     *
     * @return the single instance of the BoardFactory
     */
    public static BoardFactory getInstance() {
        if (instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    /**
     * Return a list of boards
     */
    public List<String> getBoardName(){
        return Arrays.asList("Basic Board","Advanced Board");
    }

    /**
     * Creates a new board of given name of a board, which indicates
     * which type of board should be created. For now the name is ignored.
     *
     * @param name the given name board
     * @return the new board corresponding to that name
     */
    public Board createBoard(String name) {
        Board board;
        if (name == null) {
            board = new Board(8,8, "<none>");
        } else {
            board = new Board(8,8, name);
        }
switch (name){
    case "Basic Board":
        createBasicBoard(board);
        break;
    case "Advanced Board":
        createAdvancedBoard(board);
        break;
    default: System.out.println("Unknown board:"+name);
}
        return board;
    }

    /** Using Object[][] to initialize the boards data
     * each row represent a space on board,
     * where the first two column define the coordinates (x,y) of space.
     * and the remaining column contain elements like walls, conveyor belt, gear, checkpoint
     *  Then use the function applyBoardConfig() to process and apply this data to the board.
     * @param board the board where spaces and their elements will be configured.
     */
    private static void createBasicBoard(Board board){
        Object[][] boardConfig = {
                {3, 4, new Walls(Walls.WallType.VERTICAL,Heading.WEST)},
                {6, 5, new Walls(Walls.WallType.VERTICAL,Heading.SOUTH)},
//                {1, 1, new ConveyorBelt()},
                {5, 5, new Checkpoint(1,false)},
                {4,6, new Gear(Gear.GearType.LEFT)}


        };

        applyBoardConfig(board, boardConfig);
    }
    private static void createAdvancedBoard(Board board){
        Object[][] boardConfig = {



        };

        applyBoardConfig(board, boardConfig);
    }

    /** The method is used to process an Object[][] configuration and applies it to the given board.
     * Iterates through each row to extract coordinates and board elements,
     * then assigns these elements to the corresponding spaces on the board.
     * @param board the board to which the configuration will be applied.
     * @param boardConfig the Object[][] containing board elements
     * "config" represents a row in Object[][], which contains the data for a space.
     */
    private static void applyBoardConfig(Board board, Object[][] boardConfig) {
        for (Object[] config : boardConfig) {
            int x = (int) config[0];
            int y = (int) config[1];
            Space space = board.getSpace(x, y);

            for (int i = 2; i < config.length; i++) {
                Object component = config[i];
                if (component instanceof Walls) {
                    space.getWalls().add(((Walls) component).getDirection());

                } else if (component instanceof ConveyorBelt) {
                    space.getActions().add((ConveyorBelt) component);
                } else if (component instanceof Gear) {
                    space.getActions().add((Gear) component);
                } else if (component instanceof Checkpoint) {
                    space.getActions().add((Checkpoint) component);
                }
            }
        }
    }
}

