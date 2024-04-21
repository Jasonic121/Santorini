package com.santorini;

public class PanGodCard extends GodCard {
    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y, Game game) {
        // No action required
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
        // Implement Pan's win condition logic
        return false;
    }
}