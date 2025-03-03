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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;
import javafx.scene.shape.Line;
import dk.dtu.compute.se.pisd.roborally.model.Heading;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

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

            // kiem tra tranh them trung lap
            if (!this.getChildren().contains(arrow)) {
                this.getChildren().add(arrow);
            }
        }
//    public void dispose() {
//            space.detach(this);
        }
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            drawWalls(); // ve tuong truoc
            updatePlayer(); // Sau do ve nguoi choi ( neu co)

            // XXX A3: drawing walls and action on the space (could be done
            //         here); it would be even better if fixed things on
            //         spaces  are only drawn once (and not on every update)
        }
    }

    /**
     * Draw the wall icons
     */

private void drawWalls(){

    for (Heading wall : space.getWalls()) {
        Line wallLine = new Line();

        double startX = 0, startY = 0, endX = 0, endY = 0;

        switch (wall) {
            case NORTH:
                startX = 0;
                startY = 0;
                endX = SPACE_WIDTH;
                endY = 0;
              //  if (wall == Heading.NORTH) {
                    wallLine.setTranslateY(SPACE_HEIGHT / 2.0);
                break;
            case EAST:
                startX = SPACE_WIDTH;
                startY = 0;
                endX = SPACE_WIDTH;
                endY = SPACE_HEIGHT;
                wallLine.setTranslateX(-SPACE_WIDTH / 2.0);
                //wallLine.setTranslateY(SPACE_HEIGHT / 2.0);
                break;
            case SOUTH:
                startX = 0;
                startY = SPACE_HEIGHT;
                endX = SPACE_WIDTH;
                endY = SPACE_HEIGHT;
                wallLine.setTranslateY(SPACE_HEIGHT / 2.0);
                break;
            case WEST:
                startX = 0;
                startY = 0;
                endX = 0;
                endY = SPACE_HEIGHT;
                wallLine.setTranslateX(-SPACE_WIDTH / 2.0);
                break;
        }
        wallLine.setStartX(startX);
        wallLine.setStartY(startY);
        wallLine.setEndX(endX);
        wallLine.setEndY(endY);
        wallLine.setStroke(Color.RED); // to mau tuong (co the doi mau khac)
        wallLine.setStrokeWidth(4); // Do day cua tuong

//        if (wall == Heading.NORTH) {
//            wallLine.setTranslateY(SPACE_HEIGHT / 2.0);
//        } else if (wall == Heading.SOUTH) {
//            wallLine.setTranslateY(SPACE_HEIGHT / 2.0);
//        } else if (wall == Heading.WEST) {
//            wallLine.setTranslateX(-SPACE_WIDTH / 2.0);
//        } else if (wall == Heading.EAST) {
//            wallLine.setTranslateX(-SPACE_WIDTH / 2.0);
//        }

//        wallLine.setTranslateX(-SPACE_WIDTH / 2.0);
//        wallLine.setTranslateX(-SPACE_HEIGHT/ 2.0);
        this.getChildren().add(wallLine);
    }
}

/**
 * Draws the action fields on the space (conveyor, gear and checkpoints)
 */
private void drawActionField(){
    if (space.hasConveyor()) {
        drawConveyor(space.getConveyorHeading());
    }
    if (space.isCheckpoint()) {
        drawCheckpoint(space.getCheckpointNumber());
    }
//    if (space.getGear() != null) {
//        drawGear(space.getGear().isClockwise());
//    }
    if (space.hasLaser()){
        drawLaser();
    }
}
private void drawConveyor(Heading direction) {
    Polygon arrow = new Polygon(10, 10 , 30, 20, 10,30);
    arrow.setFill(Color.BLUE);
    arrow.setRotate(90 * direction.ordinal());

    this.getChildren().add(arrow);


}
private void drawCheckpoint(int checkpointNumber) {
    javafx.scene.text.Text text = new javafx.scene.text.Text(String.valueOf(checkpointNumber));
    text.setFill(Color.YELLOW);
    text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    this.getChildren().add(text);
}
private void drawLaser() {
    Line laserLine = new Line(SPACE_WIDTH /2.0, 0, SPACE_WIDTH / 2.0, SPACE_HEIGHT);
    laserLine.setStroke(Color.RED);
    laserLine.setStrokeWidth(2);
    laserLine.getStrokeDashArray().addAll(5.0, 5.0);

    this.getChildren().add(laserLine);
}
public void dispose() {
    space.detach(this);
}
}
