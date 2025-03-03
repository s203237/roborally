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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    private Player player;


    // XXX A3
    private List<Heading> walls = new ArrayList<>();

    // XXX A3
    private List<FieldAction> actions = new ArrayList<>();
    //Checkpoint
    private int checkpointNumber = -1; // -1 nghia la khong phai checkpoint
    // Conveyor
    private Heading conveyorHeading = null; // huong cua bang chuyen  ( neu co)
    //Laser
    private boolean hasLaser = false; // kiem tra o co Laser hay khong


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * Returns the walls (actually their direction) on this space.
     * Note that clients may change this list; this should, however,
     * be done only during the setup of the game (not while the game
     * is running).
     *
     * @return the list of walls on this space
     */
    // XXX A3
    public List<Heading> getWalls() {
        return walls;
    }
//    public boolean setWalls(Heading walls) {
//        this.walls = new ArrayList<>(newWalls);
//        notifyChange();
//
//        //Tilføje en væg til array walls
//    }
    // them 1 buc tuong vao danh sach neu chua ton tai
    public void addWall (Heading wall) {
        if (!walls.contains(wall)) {
            walls.add(wall);
            notifyChange();
        }
    }
    // Xoa 1 buc tuong khoi danh sach
    public void removeWall(Heading wall) {
        if (walls.contains(wall)) {
            walls.remove(wall);
                notifyChange(); // cap nhat thay doi
            }
        }
        //kiem tra xem o nay co tuong o huong cu the khong
    public boolean hasWall (Heading direction) {
        return walls.contains(direction);
    }
    public boolean isCheckpoint() {

        return checkpointNumber > 0;
    }
    public int getCheckpointNumber() {
        return checkpointNumber;
    }
    public void setCheckpointNumber(int number) {
        this.checkpointNumber = number;
        notifyChange();
    }
    public boolean hasConveyor() {
       return conveyorHeading != null;
    }
    public Heading getConveyorHeading() {
        return conveyorHeading;
       // notifyChange();
    }
    // quan ly Laser
    public boolean hasLaser() {
        return hasLaser;
    }

    public void setLaser(boolean laser) {
        this.hasLaser = laser;
        notifyChange();
    }



    /**
     * Returns the list of field actions on this space.
     * Note that clients may change this list; this should, however,
     * be done only during the setup of the game (not while the game
     * is running).
     *
     * @return the list of field actions on this space
     */
    // XXX A3
    public List<FieldAction> getActions() {
        return actions;
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}
