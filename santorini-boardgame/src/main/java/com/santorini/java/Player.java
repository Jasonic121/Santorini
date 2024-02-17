package com.santorini.java;
import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Worker> workers;


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

    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers.get(workerIndex).moveWorkerToCell(destinationCell);
    }

    public void build(int workerIndex, Cell targetCell) {
        workers.get(workerIndex).buildAt(targetCell);
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
}

