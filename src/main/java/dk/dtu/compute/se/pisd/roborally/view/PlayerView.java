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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class PlayerView extends Tab implements ViewObserver {

    private Player player;

    private VBox top;

    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;

    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;

    private VBox buttonPanel;

    private Button finishButton;
    private Button executeButton;
    private Button stepButton;

    private VBox playerInteractionPanel;

    private GameController gameController;

    /**
     * Constructs a new PlayerView for the given player in the game.
     * @param gameController The game controller managing the game
     * @param player The player associated with this view
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        this.setContent(top);

        this.gameController = gameController;
        this.player = player;
        // Label for displaying the program cards section
        programLabel = new Label("Program");
        // GridPane for holding the program cards
        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        // Initialize the program card fields
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // FIXME the following buttons should actually not be on the tabs of the individual
        //       players, but on the PlayersView (view for all players). This should be
        //       refactored.

        // TODO V2: the following buttons should be associated with the proper methods
        //          in the game controller

        // Buttons for controlling the player's actions
        finishButton = new Button("Finish Programming");
        finishButton.setOnAction( e -> gameController.finishProgrammingPhase());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction( e-> gameController.executePrograms());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction( e-> gameController.executeStep());

        // Panel for buttons
        buttonPanel = new VBox(finishButton, executeButton, stepButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // Panel for interactive player options
        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);
        // Label for displaying command cards section
        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        // Initialize the command card fields
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }
        // Add UI components to the view
        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        // TODO A3 add a label for the status of this player could be added here
        //      ege showing the number of achieved chekpoints (etc).

        // Attach this view to the player's board and update the UI
        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
        }
    }

    /**
     * Updates the player view based on the current state of the game board.
     *
     * @param subject The game board that is being observed.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == player.board) {
            // TODO A3 update the status label for this player
            // Update the display of program cards based on the game phase
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.PROGRAMMING ) {
                        cardFieldView.setBorder(CardFieldView.BORDER_DEFAULT);
                    } else {
                        if (i < player.board.getStep()) {
                            cardFieldView.setBorder(CardFieldView.BORDER_DONE);
                        } else if (i == player.board.getStep()) {
                            if (player.board.getCurrentPlayer() == player) {
                                cardFieldView.setBorder(CardFieldView.BORDER_ACTIVE);
                            } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board.getPlayerNumber(player)) {
                                cardFieldView.setBorder(CardFieldView.BORDER_DONE);
                            } else {
                                cardFieldView.setBorder(CardFieldView.BORDER_READY);
                            }
                        } else {
                            cardFieldView.setBorder(CardFieldView.BORDER_DEFAULT);
                        }
                    }
                }
            }
            // Manage buttons based on the game phase
            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                switch (player.board.getPhase()) {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        //     from the initialization phase to the programming phase somehow!
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        break;

                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                }
            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();
                // If the current player is in an interaction phase, show options
                if (player.board.getCurrentPlayer() == player) {
                    // TODO V3: these buttons should be shown only when there is
                    //      an interactive command card, and the buttons should represent
                    //      the player's choices of the interactive command card. The
                    //      following is just a mockup showing two options
                    Button optionButton = new Button("Option1");
                    optionButton.setOnAction( e -> gameController.notImplemented());
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);

                    optionButton = new Button("Option 2");
                    optionButton.setOnAction( e -> gameController.notImplemented());
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);
                }
            }
        }
    }

}

