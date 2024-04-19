package com.santorini;

public class MinotaurGodCard extends GodCard {
    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y) {
        // Implement Minotaur's move logic
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
        // No additional win condition for Minotaur
        return false;
    }
}