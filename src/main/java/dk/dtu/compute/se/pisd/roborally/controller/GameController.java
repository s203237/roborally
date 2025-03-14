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
            board.setCounterSteps(board.getCounterSteps() + 1);
        }

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

    public void gameFinishedPhase(){
        //When someone wins
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

    public void setLastCheckpointNumber(){
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

    public boolean hasWon(Player player, Checkpoint checkpoint){
        if(checkpoint.lastCheckpoint() && this.winner==null){
            winner=player; //assign winner to the player
            System.out.println(player + " has won!");
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
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
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
    // TODO V2
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                Player other = target.getPlayer();
                Space nextSpace = board.getNeighbour(target, heading);

                if (other != null && nextSpace != null && nextSpace.getPlayer() == null) {
                    other.setSpace(nextSpace);
                }

                if (other == null || nextSpace != null && nextSpace.getPlayer() == null)
                    player.setSpace(target);

                    //Activate do action on spaces, when player moves on a space.
                    for(FieldAction actions: target.getActions()){
                        actions.doAction(this, target);
                    }

            }
        }

    }

    // TODO V2
    /**
     * Move the player forward two cells in the direction they are facing
     * @param player the player who is attempting to move forward
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    // TODO V2

    /**
     * Method is used to turn players direction to the right
     * @param player the player who is attempting to turn right
     */
    public void turnRight(@NotNull Player player) {
        Heading heading = player.getHeading();
        player.setHeading(heading.next());
    }

    // TODO V2

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
//        makeUTurn(player);
    }
    /**
     * Method is used to turn players direction to backward
     * @param player the player who is attempting to make u_turn
     */
    public void makeUTurn(@NotNull Player player) {
        Heading heading = player.getHeading();
        player.setHeading(heading.next().next());
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
