package com.santorini;

import java.util.Arrays;

public class GameState {
    private Cell[] cells;
    private Player winner;
    private int currentPlayerId;
    private Game game;
    private Cell[] validCells;
    private int gamePhase;
    private int workerPhase;
    private boolean secondBuild; // Added secondBuild variable

    public GameState(Cell[] cells, Game game, Cell[] validCells, int gamePhase, int workerPhase, boolean secondBuild) {
        this.cells = cells;
        this.game = game;
        this.winner = null;
        this.currentPlayerId = game.getCurrentPlayer().getPlayerId();
        this.validCells = validCells;
        this.gamePhase = gamePhase;
        this.workerPhase = workerPhase;
        this.secondBuild = secondBuild; 
    }

    public static GameState getGameState(Game game) {
        Cell[] cells = game.getBoard().getGrid();
        Cell[] validCells = game.getValidCells();
        int gamePhase = game.getGamePhase();
        int workerPhase = game.getWorkerPhase();
        boolean secondBuild = game.getIsSecondBuild();
        return new GameState(cells, game, validCells, gamePhase, workerPhase, secondBuild);
    }

    public int getGamePhase() {
        return this.gamePhase;
    }

    public int getWorkerPhase() {
        return this.workerPhase;
    }

    public int getWinner() {
        if (this.game.getWinner() == null)
            return -1;
        else
            return this.game.getWinner().getPlayerId();
    }

    public int getCurrentPlayer() {
        return this.currentPlayerId;
    }

    public String getValidCells() {
        return this.validCells.toString();
    }

    public boolean isSecondBuild() {
        return this.secondBuild;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"cells\":%s,\"winner\":%d,\"currentPlayer\":%d,\"gamePhase\":%d,"
            + "\"workerPhase\":%d,\"validCells\":%s,\"secondBuild\":%b}",
            Arrays.toString(this.cells), this.getWinner(), this.getCurrentPlayer(),
            this.getGamePhase(), this.getWorkerPhase(), Arrays.toString(this.validCells),
            this.isSecondBuild()
        );
    }
}