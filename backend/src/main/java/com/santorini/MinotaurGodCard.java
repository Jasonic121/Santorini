package com.santorini;

public class MinotaurGodCard extends GodCard {
    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onMove(Player player, int workerId, int x, int y, Game game) {
        Board board = game.getBoard();
        Cell targetCell = board.getCell(x, y);

        if (targetCell.isOccupied() && targetCell.getWorker().getOwner() != player) {
            // Check if the opponent worker can be forced one space straight backwards
            int opponentX = targetCell.getX();
            int opponentY = targetCell.getY();
            int forcedX = opponentX + (opponentX - player.getWorker(workerId).getCurrentCell().getX());
            int forcedY = opponentY + (opponentY - player.getWorker(workerId).getCurrentCell().getY());

            if (forcedX >= 0 && forcedX < board.getGridLength() && forcedY >= 0 && forcedY < board.getGridLength()) {
                Cell forcedCell = board.getCell(forcedX, forcedY);
                if (!forcedCell.isOccupied() && !forcedCell.hasDome()) {
                    // Move the opponent worker to the forced cell
                    Worker opponentWorker = targetCell.getWorker();
                    opponentWorker.setCurrentCell(forcedCell);
                    forcedCell.setWorker(opponentWorker);
                    targetCell.setWorker(null);

                    // Move the current worker to the target cell
                    player.moveWorker(workerId, targetCell);
                    return;
                }
            }
        }

        // If the forcing move is not possible, perform the regular move
        super.onMove(player, workerId, x, y, game);
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