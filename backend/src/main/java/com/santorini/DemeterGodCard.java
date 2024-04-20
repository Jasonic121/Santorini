package com.santorini;

public class DemeterGodCard extends GodCard {
    private boolean hasUsedExtraBuild;

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
        if (!hasUsedExtraBuild) {
            player.setBuildPoints(player.getBuildPoints() + 1);
            System.out.println("Demeter: Build points set to: " + player.getBuildPoints());
        }
    }

    @Override
    public void onAfterBuild(Player player, int workerId, int x, int y) {
        if (!hasUsedExtraBuild) {
            hasUsedExtraBuild = true;
        } else {
            System.out.println("Demeter: Extra build used");
            player.resetActionPoints();
            hasUsedExtraBuild = false;
        }
    }

    @Override
    public boolean checkWinCondition(Player player) {
        // No additional win condition for Demeter
        return false;
    }
}