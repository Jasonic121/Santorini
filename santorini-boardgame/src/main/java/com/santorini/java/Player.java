package com.santorini.java;
import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Worker> workers;


    public Player(int playerId) {
        this.playerId = playerId;
        this.workers = new ArrayList<Worker>();
        // Each player starts with two workers.
        this.workers.add(new Worker(this, 1));
        this.workers.add(new Worker(this, 2));
    }

    // Initial placement of workers
    public void placeInitialWorker(int workerIndex, Cell initialCell) {
        workers.get(workerIndex).moveWorkerToCell(initialCell);
    }

    public boolean checkMoveAvailable() {
        return board.isOccupied(x, y);
    }

    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers.get(workerIndex).moveWorkerToCell(destinationCell);
    }

    public void build(int workerIndex, int x, int y) {
        workers.get(workerIndex).buildAt(x, y);
    }
}

