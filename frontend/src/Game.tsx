interface GameState {
  cells: Cell[];
  winner: string | null;
  currentPlayer: number;
  validCells: Cell[];
}

interface Cell {
  x: number;
  y: number;
  height: number;
  hasDome: boolean;
  occupied: boolean;
  occupiedBy: number | null;
}

export type { GameState };