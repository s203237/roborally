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
    private static void createBasicBoard(Board board){
        Object[][] boardConfig = {
//                {0, 0, new Wall(Heading.SOUTH), new ConveyorBelt(Heading.WEST)},
//                {1, 0, new Wall(Heading.NORTH), new ConveyorBelt(Heading.WEST)},
//                {1, 1, new Wall(Heading.WEST), new ConveyorBelt(Heading.NORTH)},
//                {5, 5, new Wall(Heading.SOUTH), new ConveyorBelt(Heading.WEST)},
//                {6, 5, new ConveyorBelt(Heading.WEST)}
        };

        applyBoardConfig(board, boardConfig);
    }
    private static void createAdvancedBoard(Board board){
        Object[][] boardConfig = {
//                {0, 0, new Wall(Heading.SOUTH), new ConveyorBelt(Heading.WEST)},
//                {1, 0, new Wall(Heading.NORTH), new ConveyorBelt(Heading.WEST)},
//                {1, 1, new Wall(Heading.WEST), new ConveyorBelt(Heading.NORTH)},
//                {5, 5, new Wall(Heading.SOUTH), new ConveyorBelt(Heading.WEST)},
//                {6, 5, new ConveyorBelt(Heading.WEST)},
//                {2, 2, new Gear(Gear.GearType.LEFT)},
//                {4, 4, new Gear(Gear.GearType.RIGHT)},
//                {3, 3, new Checkpoint(1)},
//                {6, 6, new Checkpoint(2)}
        };

        applyBoardConfig(board, boardConfig);
    }
    private static void applyBoardConfig(Board board, Object[][] boardConfig) {
        for (Object[] config : boardConfig) {
            int x = (int) config[0];
            int y = (int) config[1];
            Space space = board.getSpace(x, y);

//            for (int i = 2; i < config.length; i++) {
//                Object component = config[i];
//                if (component instanceof Wall) {
//                    space.getWalls().add(((Wall) component).getHeading());
//                } else if (component instanceof ConveyorBelt) {
//                    space.getActions().add((ConveyorBelt) component);
//                } else if (component instanceof Gear) {
//                    space.getActions().add((Gear) component);
//                } else if (component instanceof Checkpoint) {
//                    space.getActions().add((Checkpoint) component);
//                }
//            }
        }
    }
}

