package com.santorini;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents the state of a game.
 * This class stores information about the cells, winner, current player, next move
 */
public class GameState {
    private Cell[] cells;
    private Player winner;
    private Player currentPlayer;
    private Game game;


    public GameState(Cell[] cells, Game game) {
        this.cells = cells;
        this.game = game;
        this.winner = null;
        this.currentPlayer = game.getCurrentPlayer();
    }

    /**
     * Creates a new GameState object for the given game.
     *
     * @param game the game for which to create the state
     * @return the new GameState object
     */
    public static GameState getGameState(Game game) {
        Cell[] cells = game.getBoard().getGrid();
        return new GameState(cells, game);
    }


    /** Getters and Setters **/
    /**
     * Returns the cells of the game state.
     *
     * @return an array of Cell objects representing the cells of the game state
     */
    public Cell[] getCells() {
        Cell[] gameCells = new Cell[25];
        Board board = game.getBoard();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cell cell = board.getCell(i, j);
                int height = cell.getHeight();
                boolean hasDome = cell.hasDome();
                boolean isOccupied = cell.isOccupied();

                if (isOccupied) {
                    for (Player player : game.getPlayers()) {
                        for (int k = 0; k < player.getWorkerAmount(); k++) {
                            if (player.getWorkerCurrentCell(k).equals(cell)) {
                                break;
                            }
                        }
                    }
                }

                gameCells[5 * i + j] = new Cell(i, j, height, hasDome, isOccupied);
            }
        }

        return gameCells;
    }

    /**
     * Returns the winner of the game state.
     *
     * @return the winner of the game state, or "null" if there is no winner
     */
    public String getWinner() {
        if (this.winner == null)
            return "null";
        else
            return this.winner.toString();
    }

    /**
     * Returns the current player in the game state.
     *
     * @return the current player in the game state, or "null" if there is no current player
     */
    public String getCurrentPlayer() {
        if (this.currentPlayer == null)
            return "null";
        else
            return this.currentPlayer.toString();
    }

    /**
     * Returns the string representation of the GameState object in JSON format.
     *
     * @return the string representation of the GameState object
     */
    // @Override
    // public String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("{ \"cells\": [");
    //     for (int i = 0; i < cells.length; i++) {
    //         sb.append(cells[i].toString());
    //         if (i < cells.length - 1) {
    //             sb.append(", ");
    //         }
    //     }
    //     sb.append("], \"winner\": \"");
    //     sb.append(this.getWinner());
    //     sb.append("\", \"currentPlayer\": \"");
    //     sb.append(this.getCurrentPlayer());
    //     sb.append("\"}");
    //     return sb.toString();
    // }
    @Override
    public String toString() {
        return String.format(
            "{\"cells\":%s,\"winner\":\"%s\",\"currentPlayer\":\"%s\"}",
            Arrays.toString(this.cells), this.getWinner().toString(), this.getCurrentPlayer().toString()
        );
    }
}