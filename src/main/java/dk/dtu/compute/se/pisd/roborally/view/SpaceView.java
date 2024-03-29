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
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 55; // 60; // 75;
    final public static int SPACE_WIDTH = 55;  // 60; // 75;

    public final Space space;
    private Label statusLabel;


    public SpaceView(@NotNull Space space) {
        this.space = space;
        statusLabel = new Label("<no status>");

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);


        this.getChildren().add(statusLabel);

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
            updateWall();
            updateConveyorBelt();
            updateCheckpoint();
            updateGears();
            updatePlayer();
        }
    }

    /**
     * This method is used to determine how the walls are created in the space.
     */
    public void updateWall() {
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
     * Methods are used to determine how the board elements are created in the space.
     */
    // TODO Conveyorbelt bliver ikke længere vist og ved ikke hvorfor
    public void updateConveyorBelt() {
        ConveyorBelt conveyorBelt = space.getConveyorBelt();
        if (conveyorBelt !=null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    SPACE_WIDTH/ 2-3, SPACE_HEIGHT -5,
                    SPACE_HEIGHT -5, 0.0);
            try {
                arrow.setFill(Color.rgb(152,251,152));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }
            arrow.setRotate((90 * conveyorBelt.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    public void updateGears() {
        Gear gear = space.getGear();
        if(gear!=null){
            ImageView imageview =new ImageView();
            Image image = new Image("Images/gear.png",50,50,false,false);
            imageview.setImage(image);
            this.getChildren().add(imageview);
        }
    }
    /**
     * This method is used to determine how the checkpoints are created in the space.
     */
    public void updateCheckpoint() {
        Checkpoint checkpoint = space.getCheckpoint();
        if (checkpoint!= null) {
            Circle circle = new Circle();
            circle.setCenterX(25);
            circle.setCenterY(25);
            circle.setRadius(25);
            try {
                circle.setFill(Color.YELLOW);
            } catch (Exception e) {
                circle.setFill(Color.MEDIUMPURPLE);
            }
            this.getChildren().add(circle);
            Text t =new Text();
            t.setText(String.valueOf("Check\npoint."+checkpoint.getCheckpoint()));
           this.getChildren().add(t);
        }
    }

}




