package com.santorini;

public class PanGodCard extends GodCard {
    private static final int END_GAME_PHASE = 4;

    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y, Game game) {
        // No action required
    }

    @Override
    public void onMove(Player player, int workerId, int x, int y, Game game) {
        // Get the worker's current cell before the move
        Worker worker = player.getWorker(workerId);
        Cell previousCell = worker.getCurrentCell();

        // Call the default implementation to perform the regular move
        super.onMove(player, workerId, x, y, game);

        // Get the worker's current cell after the move
        Cell currentCell = worker.getCurrentCell();

        // Check if the worker moved down two or more levels
        if (currentCell.getHeight() <= previousCell.getHeight() - 2) {
            game.setWinner(player.getPlayerId());
            game.setGamePhase(END_GAME_PHASE); // Set the game phase to end game
            System.out.println("Player " + player.getPlayerId() + " wins by moving down two or more levels!");
        }
    }

    @Override
    public void onAfterMove(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onBeforeBuild(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onAfterBuild(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public boolean checkWinCondition(Player player) {
        // No additional win condition for Pan
        return false;
    }
}