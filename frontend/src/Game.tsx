interface GameState {
  cells: Cell[];
  winner: number;
  currentPlayer: number;
  validCells: Cell[];
  gamePhase: number;
  workerPhase: number;
}

interface Cell {
  x: number;
  y: number;
  height: number;
  hasDome: boolean;
  occupied: boolean;
  occupiedBy: number | null;
}

export type { GameState, Cell};