package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Checkpoint extends FieldAction {
    final private int checkpointNumber;
    private boolean lastCheckpoint;//The checkpoint-number

    public Checkpoint(int checkpointNumber, boolean lastCheckpoint){
        this.checkpointNumber = checkpointNumber;
        this.lastCheckpoint = lastCheckpoint;
    }

    public boolean lastCheckpoint(){
        return this.lastCheckpoint;
    }

    public void setLastCheckpoint(boolean lastCheck){
        this.lastCheckpoint=lastCheck;
    }

    public int getCheckPointNumber(){
        return this.checkpointNumber;
    }

    /** Our checkpoint checks for whether this checkpoint is the player's
     * next checkpoint, using the player's checkpointProgress attribute.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space.getPlayer()==null){
            return false;
        }

        Player player = space.getPlayer();

        //If player's next checkpoint is this checkpoint,
        //set the players checkpoint progress to this checkpoints
        if(player.getCheckpointProgress()+1==this.getCheckPointNumber()){
            player.setCheckpointProgress(this.getCheckPointNumber());

            //Check if player has won:
            gameController.hasWon(player, this);
            return true;
        }
        return false;
    }
}
