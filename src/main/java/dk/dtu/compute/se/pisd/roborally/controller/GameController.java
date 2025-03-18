/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    Player winner;

    public GameController(@NotNull Board board) {

        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {

        Player current = board.getCurrentPlayer();// create a player
        if (space != null && space.getPlayer() == null) {
            current.setSpace(space);
            int n = board.getPlayerNumber(current);
            Player next = board.getPlayer((n + 1) % board.getPlayersNumber());
            board.setCurrentPlayer(next);
            //Increment steps
            board.setCounter(board.getCounter() + 1);
        }

    }


    /**
     * Used for moving the player to a given space,
     * and push neighbour players using recursion
     * @param pusher The player getting moved
     * @param space The space which the player tries to move towards
     * @param heading The direction of the movement
     * @throws IllegalStateException gets thrown if any walls prevent pushing neighbour players
     */
    public void moveToSpace(@NotNull Player pusher, @NotNull Space space, @NotNull Heading heading) throws IllegalStateException {

        Player pushed = space.getPlayer();

        //Push neighbor players
        if(pushed!=null){
            Space nextSpace = board.getNeighbour(space, heading);
            //Check next space whether there is a wall or not.
            if(nextSpace != null){
                moveToSpace(pushed, nextSpace, heading);
            }else{
                throw new IllegalStateException("Not able to move player to space");
            }

        }

        //Move actual player to given space
        pusher.setSpace(space);
        //Activate do action on the space.
        for(FieldAction actions: space.getActions()){
            actions.doAction(this, space);
        }
        board.setCounter(board.getCounter()+1);
    }

    // XXX V2

    /**
     * Start the programming phase, sets the first player, and distributes command cords.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                // clear previously programmed commands
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                // distribute new command cards to the player
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX V2

    /**
     * Generates a random command card from the available commands.
     * @return commandCard the randomly selected command card.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX V2

    /**
     * Ends the programming phase and transitions to the activation phase.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    /** Creates a pop-up window, showing the player has won,
     * and changes the game phase.
     *
     */
    public void gameWonPhase(){
        board.setPhase(Phase.PLAYER_WON);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("CONGRATULATION!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulation!! "+board.getCurrentPlayer().getName()+" is won!");
        alert.showAndWait();
    }

    // XXX V2

    /**
     * Makes the program field of the players visible at the specified index.
     * @param register the index of the program field to be made visible.
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX V2

    /**
     * Hides all program fields of the players.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Iterates through all the spaces on the board, and compares
     * each checkpoint's number with each other, and returns the space
     * which contains the highest (last) checkpoint.
     * @return
     */
    public Space getLastCheckpointSpace(){
        int highestCheckpoint=0;
        Space spaceLastCheckpoint = null;

        for(var i=0; i<board.width; i++){
            for(var j=0; j<board.height; j++){

                for(FieldAction action: board.getSpace(i, j).getActions())
                    if(action instanceof Checkpoint){
                        if(((Checkpoint) action).getCheckPointNumber()>highestCheckpoint){
                            highestCheckpoint=((Checkpoint) action).getCheckPointNumber();
                            spaceLastCheckpoint=board.getSpace(i,j);
                        }
                    }
            }
        }

        if(spaceLastCheckpoint==null){
            throw new IllegalStateException("No last checkpoint");
        }

        return spaceLastCheckpoint;
    }

    /**
     * Finds the last checkpoint's space (rated through highest checkpoint number)
     * with getLastCheckpoint number, and assigns this space's checkpoint as lastCheckpoint.
     */
    public void updateLastCheckpointNumber(){
        try{
            Space lastCheckpointSpace = getLastCheckpointSpace();
            for(FieldAction actions: lastCheckpointSpace.getActions()){
                if(actions instanceof Checkpoint){
                    ((Checkpoint) actions).setLastCheckpoint(true);
                }
            }

        }catch(IllegalStateException err){
            System.out.println(err.getMessage());
        }
    }

    /**
     * We use this method on checkpoint collisions with players to check for every
     * checkpoint if the player has won (collides with last checkpoint), and changes
     * the game state to PLAYER_WON
     * @param player
     * @param checkpoint
     * @return true or false if player has won
     */
    public boolean hasWon(Player player, Checkpoint checkpoint){
        if(checkpoint.lastCheckpoint() && this.winner==null){
            winner=player; //assign winner to the player
            gameWonPhase();
            return true;
        }
        return false;
    }

    // XXX V2

    /**
     * Executes all player's programs in the activation phase
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX V2
    /**
     * Executes the program step by step.
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX V2
    /**
     * Continues executing programs until the activation phase is completed.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX V2
    /**
     * Executes the next step in the current player's program.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;

                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    actionFileds(step);

                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }
    /**
     * Executes all field actions for players if the game has completed the programming phase.
     * This method iterates over all players, retrieves their current space, and triggers all
     * field actions associated with that space.
     * @param step the current step in the game, used to determine if actions should be executed.
     *             Actions are executed only when the step is greater than or equal to the
     *             number of registers in a player's program.
     */
    public void actionFileds(int step){
        if(step>=Player.NO_REGISTERS){
            for(int i =0;i<board.getPlayersNumber();i++){
                Player player = board.getPlayer(i);
                Space playerSpace = player.getSpace();

                for(FieldAction action: playerSpace.getActions()){
                    action.doAction(this, playerSpace);

                }
            }
        }
    }
    /**
     * Executes a command for the current player and proceeds with the game flow.
     * This method checks whether the game is in the correct phase and if the given command is valid.
     * It then executes the command for the current player and transitions the game to the next step
     * or phase accordingly.
     *
     * @param option The command to be executed for the current player. Must not be null.
     */
public void executeCommandOptionAndContinue(@NotNull Command option){
    // Retrieve the current player
        Player currentPlayer = board.getCurrentPlayer();
        //ensure the current player is valid, the game is in PLAYER_INTERACTION, and option is not null
        if(currentPlayer!=null && board.getPhase()==Phase.PLAYER_INTERACTION&& option!=null){
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer,option);
            int nextPlayerNumber = board.getPlayerNumber(currentPlayer)+1;
            if(nextPlayerNumber< board.getPlayersNumber()){
                // set the next player as the current player
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            }else{
                //if all players have taken their turn, move to the next step
                int step =board.getStep()+1;
                if(step< Player.NO_REGISTERS){
                    // make program fields visible for the new step
                    // and reset the current player to the first player
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                }else {
                    // if all steps are completed, transition to the programming phase
                    startProgrammingPhase();
                }
            }

        }
        continuePrograms();
}
    // XXX V2
    /**
     * Executes the given command for the specified player.
     *
     * @param player The player executing the command.
     * @param command The command to be executed.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case U_TURN:
                    this.makeUTurn(player);
                    break;
                case BACKWARD:
                    this.moveBackward(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }


    /**
     * Move the player forward in the direction they are facing
     * First check if the player is on the correct board
     * then determine the target space based on the player's current heading.
     * if there is a wall blocking movement, the player remains in place.
     * if the target space contains another player, the method attempts to push
     * that player to the next available space in the same direction.
     * if the target space is available, the player moves to the new position.
     * if the adjacent space is occupied and can not be pushed, the player remains in place.
     *
     * @param player the player who is attempting to move forward
     */
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);


            //Target may not be null - if it is null, there is a wall
            if(target!=null){
                try{
                    //We move player using MoveToSpace to make sure other players get pushed aswell
                    moveToSpace(player, target, heading);
                }catch(IllegalStateException err){
                    System.out.println(err.getMessage());
                }
            }


        }
    }
//    /**
//     * This method is used to check the new space is available for the current player moves.
//     * to check if there is any player in the new space, if yes, move to make space available for current player.
//     * @param heading  the heading of the player
//     * @param player the current player
//     * @param target the target space where the current player will move to
//     * @return  true if the player successfully moves to the target space, otherwise false.
//     */
//    private boolean moveToSpace(Player player, Space target, Heading heading) {
//        if (target == null) {
//            return false;
//        }
//        Player other = target.getPlayer();
//        if(other==null){
//            player.setSpace(target);
//            return true;
//        }else if(other!=null){
//            Space nextSpace = board.getNeighbour(target,heading);
//            boolean result = moveToSpace(target.getPlayer(),nextSpace,heading);
//            if(result!=false){
//                player.setSpace(target);
//                return true;
//            }else{
//                return false;}
//        }else{
//                return true;
//
//        }
//    }

    // TODO V2

    /**
     * Move the player forward two cells in the direction they are facing
     * @param player the player who is attempting to move forward
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * Method is used to turn players direction to the right
     * @param player the player who is attempting to turn right
     */
    public void turnRight(@NotNull Player player) {
        Heading heading = player.getHeading();
        player.setHeading(heading.next());
    }


    /**
     * Method is used to turn players direction to the left
     * @param player the player who is attempting to turn left
     */
    public void turnLeft(@NotNull Player player) {
        Heading heading = player.getHeading();
        player.setHeading(heading.prev());
    }

    /**
     * Method is used to turn players direction to backward and move forward one cell.
     * @param player the player who is attempting to move backward
     */
    public void moveBackward(@NotNull Player player) {

        makeUTurn(player);
        moveForward(player);
        //makeUTurn(player);

    }
    /**
     * Method is used to turn players direction to backward
     * @param player the player who is attempting to make u_turn
     */
    public void makeUTurn(@NotNull Player player) {
        Heading heading = player.getHeading().next().next();
        player.setHeading(heading);
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet.
     * This should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
