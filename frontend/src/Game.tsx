interface GameState {
  cells: Cell[];
  winner: string | null;
  currentPlayer: string;
}

interface Cell {
  x: number;
  y: number;
  height: number;
  hasDome: boolean;
  occupied: boolean;
}

export type { GameState, Cell };