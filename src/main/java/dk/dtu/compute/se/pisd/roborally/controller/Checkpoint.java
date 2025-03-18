package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import dk.dtu.compute.se.pisd.roborally.model.Phase;

public class Checkpoint extends FieldAction {
    final private int checkpointNumber;
    final private boolean lastCheckpoint;//The checkpoint-number

    public Checkpoint(int checkpointNumber, boolean lastCheckpoint) {
        this.checkpointNumber = checkpointNumber;
        this.lastCheckpoint = lastCheckpoint;
    }

    public boolean isLastCheckpoint() { // check it is tha last checkpoint

        return this.lastCheckpoint;
    }

    public int getCheckPointNumber() {

        return this.checkpointNumber;
    }

    public boolean getLasCheckpoint() {
        return lastCheckpoint;
    }

    //public boolean setLastCheckpoint() {
    //return true;
    //}

    /**
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        if (player == null) {
            return false;
        } // step 3: 4e
        //If player's next checkpoint is this checkpoint,
        //set the players checkpoint progress to this checkpoints
        if (player.getCheckpointProgress() + 1 == this.getCheckPointNumber()) {
            player.setCheckpointProgress(this.getCheckPointNumber());

            //TODO Check if winner
            // step 3: 4e check the win condition .
            if (this.isLastCheckpoint()) {
                String winMessage = "Player " + player.getName() + " has won the game!";
                System.out.println(winMessage);

            }
            return true;
        }
        return false;
    }
}







