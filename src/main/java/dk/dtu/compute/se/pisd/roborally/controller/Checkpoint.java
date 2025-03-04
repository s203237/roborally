package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Checkpoint extends FieldAction {
    final private Space checkpointSpace; //The space on which the checkpoint exists
    final private int checkpointNumber; //The checkpoint-number

    public Checkpoint(Space checkpointSpace, int checkpointNumber){
        this.checkpointSpace = checkpointSpace;
        this.checkpointNumber = checkpointNumber;
    }

    public Space getCheckPointSpace(){
        return this.checkpointSpace;
    }

    public int getCheckPointNumber(){
        return this.checkpointNumber;
    }

    /**
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

            //TODO Check if winner
            /*if(player.getCheckpointProgress()>=getCheckpoints()){
                //Winner
            }*/

            return true;
        }
        return false;
    }
}
