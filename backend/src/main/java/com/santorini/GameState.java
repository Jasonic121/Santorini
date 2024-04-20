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

    public GameState(Cell[] cells, Game game, Cell[] validCells, int gamePhase, int workerPhase) {
        this.cells = cells;
        this.game = game;
        this.winner = null;
        this.currentPlayerId = game.getCurrentPlayer().getPlayerId();
        this.validCells = validCells;
        this.gamePhase = gamePhase;
        this.workerPhase = workerPhase;
    }

    public static GameState getGameState(Game game) {
        Cell[] cells = game.getBoard().getGrid();
        Cell[] validCells = game.getValidCells();
        int gamePhase = game.getGamePhase();
        int workerPhase = game.getWorkerPhase();
        return new GameState(cells, game, validCells, gamePhase, workerPhase);
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

    @Override
    public String toString() {
        return String.format(
            "{\"cells\":%s,\"winner\":%d,\"currentPlayer\":%d,\"gamePhase\":%d,\"workerPhase\":%d,\"validCells\":%s}",
            Arrays.toString(this.cells), this.getWinner(), this.getCurrentPlayer(), this.getGamePhase(), this.getWorkerPhase(), Arrays.toString(this.validCells)
        );
    }
}