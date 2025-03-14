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
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }


    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            // XXX A3: drawing walls and action on the space (could be done
            //         here); it would be even better if fixed things on
            //         spaces  are only drawn once (and not on every update)

            drawWalls();
            drawConveyorBelt();
            updateGears();
            updateCheckpoints();
            updatePlayer(); // Player updates last, to make sure it stays on top of other elements.

        }
    }

    /**
     * Draw the wall icons
     */

    private void drawWalls() {
        List<Heading> wallsHeading = space.getWalls();
        for (Heading wall : wallsHeading) {
            Pane pane = new Pane();
            Line line = null;
            switch (wall) {
                case EAST -> line = new Line(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                case NORTH -> line = new Line(2, 2, SPACE_WIDTH - 2, 2);
                case WEST -> line = new Line(2, 2, 2, SPACE_HEIGHT - 2);
                case SOUTH -> line = new Line(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
            }
            line.setStroke(Color.RED);
            line.setStrokeWidth(5);
            pane.getChildren().add(line);
            this.getChildren().add(pane);
        }
    }


    /**
     * This method is used to determine how the gear are created in the space
     */
    public void updateGears() {
        for (FieldAction action : space.getActions()) {
            if (action instanceof Gear) {
                Gear gear = (Gear) action;
                ImageView imageView = new ImageView();
                Image image = new Image(getClass().getResource("/Images/gear.png").toExternalForm(), 40, 40, false, false);
                imageView.setImage(image);
                this.getChildren().add(imageView);
                if(gear!=null){
                    Text i= new Text(gear.isRight()?"right":"left");
                    this.getChildren().add(i);
                }

            }

        }
    }
                /**
     * Draws the action fields on the space (conveyor, gear and checkpoints)
     */

    public void drawConveyorBelt() {
        for (FieldAction action : space.getActions()){
            if (action instanceof ConveyorBelt conveyor) {
                Heading heading = ((ConveyorBelt) action).getHeading();
                Polygon conArrow = new Polygon(
                        15.0, 0.0,
                        0.0, 30.0,
                        30.0, 30.0
                );
                conArrow.setFill(Color.LIGHTGRAY);

                switch (heading) {
                    case NORTH:
                        conArrow.setRotate(0);
                        break;
                    case EAST:
                        conArrow.setRotate(90);
                        break;
                    case SOUTH:
                        conArrow.setRotate(180);
                        break;
                    case WEST:
                        conArrow.setRotate(270);
                        break;

                }
                this.getChildren().add(conArrow);

            }
        }
    }

    public void updateCheckpoints(){
        for(FieldAction action : space.getActions()){
            if(action instanceof Checkpoint){
                int checkpointNumber = ((Checkpoint) action).getCheckPointNumber();
                Circle checkpointCircle = new Circle(space.x,space.y,16);
                checkpointCircle.setFill(Color.YELLOW);
                Text checkpointText = new Text(String.valueOf(checkpointNumber));

                this.getChildren().add(checkpointCircle);
                this.getChildren().add(checkpointText);
            }
        }
    }





}
