package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Walls extends FieldAction {
    public enum WallType {VERTICAL, HORIZONTAL}

    private final WallType type;
    private final Heading direction;

    public Walls(WallType type, Heading direction) {
        this.direction = direction ;
        this.type = type;

    }
    public Heading getDirection() {
        return direction;
    }

    @Override
    public boolean doAction (@NotNull GameController gameController, @NotNull Space space) {

        return false;
    }
    @Override
    public String toString() {
        return "Wall {  type = " + (type != null ? type : "UNKNOWN") + ", direction=" + direction + " }";
    }
}
