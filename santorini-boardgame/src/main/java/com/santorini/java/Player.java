package com.santorini.java;
import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Worker> workers;
    private int movePoints = 1;
    private int buildPoints = 1;

    public Player(int playerId) {
        this.playerId = playerId;
        this.workers = new ArrayList<Worker>();
        // Each player starts with two workers.
        this.workers.add(new Worker(this, 0));
        this.workers.add(new Worker(this, 1));
    }

    // Initial placement of workers
    public void placeWorkerOnBoard(int workerIndex, Cell initialCell) {
        workers.get(workerIndex).placeInitialWorker(initialCell);
    }

    // Move functionality
    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers.get(workerIndex).moveWorkerToCell(destinationCell);
        movePoints--;
    }

    // Build functionality
    public void build(int workerIndex, Cell targetCell) {
        workers.get(workerIndex).buildAt(targetCell);
        buildPoints--;
    }

    // Check if a player has lost
    public boolean checkLose() {
        for (Worker worker : workers) {
            if (worker.checkMovePossibilities()) {
                return true;
            }
        }
        return false;
    }

    /*
    Helper methods for reseting and checking if a player has move/build points available
    */

    // Reset action points
    public void resetActionPoints() {
        movePoints = 1;
        buildPoints = 1;
    }

    // Move points validation
    public boolean checkMovePointsAvailable() {
        // Implement logic to check if a move is available
        if (movePoints == 0) {
            return false;
        }
        return true;
    }

    // Build points validation
    public boolean checkBuildPointsAvailable() {
        // Implement logic to check if a build is available
        if (buildPoints == 0) {
            return false;
        }
        return true;
    }

    /*
    Getters for Player
    */
    public Cell getWorkerCurrentCell(int workerIndex) {
        return workers.get(workerIndex).getCurrentCell();
    }
    
    public int getWorkerAmount() {
        return workers.size();
    }

    public int getPlayerId() {
        return playerId;
    }
}

