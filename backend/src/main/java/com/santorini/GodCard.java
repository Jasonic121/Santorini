package com.santorini;

public abstract class GodCard {
    public abstract void onBeforeMove(Player player, int workerId, int x, int y);
    public abstract void onAfterMove(Player player, int workerId, int x, int y);
    public abstract void onBeforeBuild(Player player, int workerId, int x, int y);
    public abstract void onAfterBuild(Player player, int workerId, int x, int y);
    public abstract boolean checkWinCondition(Player player);
}