package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Checkpoint extends FieldAction {
    private int checkpointNumber;

    /*public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }*/


    /**
     * This method checks if there is a player located on the space.
     * If yes then checks if the players total checkpoints is less than the checkpoint number which the player has landed on
     * If yes then the player is granted a checkpoint number.
     * @param gameController
     * @param space
     * @return
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player = space.getPlayer();
            if(player != null){
                if(player.getCheckpoints()+1 == getCheckpoint()){
                    player.setCheckpoints(player.getCheckpoints()+1);
                }
            }
            return true;
        }
        return false;
    }
    public int getCheckpoint(){
        return checkpointNumber;
    }

    public void setCheckpoint(int checkpointNumber) {
        this.checkpointNumber=checkpointNumber;
    }
}
