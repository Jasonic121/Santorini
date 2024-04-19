package com.santorini;

public class DemeterGodCard extends GodCard {
    private boolean hasBuiltOnce;

    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y) {
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
        if (!hasBuiltOnce) {
            hasBuiltOnce = true;
        } else {
            player.resetActionPoints();
            hasBuiltOnce = false;
        }
    }

    @Override
    public boolean checkWinCondition(Player player) {
        // No additional win condition for Demeter
        return false;
    }
}