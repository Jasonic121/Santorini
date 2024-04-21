package com.santorini;

public class MinotaurGodCard extends GodCard {
    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y, Game game) {
        Board board = game.getBoard();
        Cell workerCell = player.getWorker(workerId).getCurrentCell();
        addOpponentWorkersToValidCells(player, workerCell, board, game);
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
                    game.setWorkerPhase(1);
                    game.setValidCells(board.validateCellsForBuilding(player.getWorker(workerId).getCurrentCell()));
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

    private void addOpponentWorkersToValidCells(Player player, Cell workerCell, Board board, Game game) {
        int currentX = workerCell.getX();
        int currentY = workerCell.getY();
    
        for (int i = currentX - 1; i <= currentX + 1; i++) {
            for (int j = currentY - 1; j <= currentY + 1; j++) {
                if (isValidPosition(i, j, currentX, currentY, board)) {
                    Cell cell = board.getCell(i, j);
                    if (isOpponentWorker(cell, player)) {
                        if (canForcedMove(currentX, currentY, i, j, board)) {
                            System.out.println("Adding opponents' cell: " + cell.getX() + ", " + cell.getY());
                            game.addValidCells(cell);
                            for (Cell validCell : game.getValidCells()) {
                                System.out.println("Valid cell: " + validCell.getX() + ", " + validCell.getY());
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isValidPosition(int x, int y, int currentX, int currentY, Board board) {
        return x >= 0 && x < board.getGridLength() && y >= 0 && y < board.getGridLength() && !(x == currentX && y == currentY);
    }
    
    private boolean isOpponentWorker(Cell cell, Player player) {
        return cell.isOccupied() && cell.getWorker().getOwner() != player;
    }
    
    private boolean canForcedMove(int currentX, int currentY, int opponentX, int opponentY, Board board) {
        int forcedX = opponentX + (opponentX - currentX);
        int forcedY = opponentY + (opponentY - currentY);
    
        if (isValidPosition(forcedX, forcedY, currentX, currentY, board)) {
            Cell forcedCell = board.getCell(forcedX, forcedY);
            return !forcedCell.isOccupied() && !forcedCell.hasDome();
        }
        return false;
    }
}