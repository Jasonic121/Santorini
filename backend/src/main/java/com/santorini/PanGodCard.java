package com.santorini;

public class PanGodCard extends GodCard {
    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onAfterMove(Player player, int workerId, int x, int y) {
        // Check if the worker moved down two or more levels
        int heightDifference = player.getWorker(workerId).getPreviousCell().getHeight() - player.getWorker(workerId).getCurrentCell().getHeight();
        if (heightDifference >= 2) {
            player.setWinCondition(true);
        }
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
        // Check if the player has won by moving down two or more levels
        return player.hasWinCondition();
    }
}