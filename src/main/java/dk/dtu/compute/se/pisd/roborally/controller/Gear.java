package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Gear extends FieldAction {
    public enum GearType{LEFT, RIGHT}

    private  GearType direction;



    public Gear(GearType direction){
        this.direction =direction;
    }

    public boolean doAction(@NotNull GameController gameController, @NotNull Space space){
        Heading newHeading ;
        if(direction == GearType.LEFT) {
            newHeading = space.getPlayer().getHeading().prev();
        }else{
            newHeading = space.getPlayer().getHeading().next();
        }
        space.getPlayer().setHeading(newHeading);
        return true;
    }

    public boolean isRight() {
        return this.direction == GearType.RIGHT;
    }


    @Override
    public String toString(){
        return "Gear {direction="+direction+"}";
    }

}
