/* es-lint-disable */
import React, { useEffect, useState } from 'react';
import { GameState, Cell} from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import map from './resources/img/background/map.png';

interface Props {}
interface State extends GameState {
  gamePhase: int; // 0 for not started, 1 for setup, 2 for playing
  selectedCells: Cell[];
}

class App extends React.Component<Props, State> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      winner: null,
      currentPlayer: 'Player 1',
      gamePhase: 0,
      selectedCells: [],
    };
  }

  newGame = async () => {
    try {
      const response = await fetch('http://localhost:8080/newgame');
      const json = await response.json();
      this.setState({
        gamePhase: 1,
        cells: json['cells'],
        selectedCells: [],
        winner: json['winner'],
        currentPlayer: json['currentPlayer'],
      });
      console.log('cells', this.state.cells);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  };

  play = (x: number, y: number): React.MouseEventHandler => {
    return async (e) => {
      e.preventDefault();

      if (this.state.gamePhase === 1) { // Setup phase
        const clickedCell = this.state.cells.find((cell) => cell.x === x && cell.y === y);
        if (clickedCell && !this.state.selectedCells.some(cell => cell.x === clickedCell.x && cell.y === clickedCell.y)) {
          this.setState((prevState) => ({
            selectedCells: [...prevState.selectedCells, clickedCell],
          }), () => {
            this.setupInitialWorker(clickedCell);
          });
        }
        console.log('Selected cells:', this.state.selectedCells);
      } else {
        const response = await fetch(`http://localhost:8080/play?x=${x}&y=${y}`);
        const json = await response.json();
        this.setState({ cells: json['cells'], winner: json['winner'], currentPlayer: json['currentPlayer'] });
      }
    };
  };

  setupInitialWorker = async (cell: Cell) => {
    const { selectedCells } = this.state;
    const cellIndex = selectedCells.length;
    const response = await fetch(`http://localhost:8080/setup?cell${cellIndex}=${cell.x},${cell.y}`);
    const json = await response.json();
    this.setState({
      cells: json['cells'],
      winner: json['winner'],
      currentPlayer: json['currentPlayer'],
    }, () => {
      if (selectedCells.length === 4) {
        this.setState({
          gamePhase: 2, // Change to playing phase
          selectedCells: [],
        });
      }
    });
  };

  /**
   * Returns the instructions based on the current game state.
   * If there is a winner, it returns the winner's name.
   * If there is no winner, it returns the current player's name and the next turn.
   * 
   * @returns The instructions as a string.
   */
  instructions(): string {
    const winner = this.state.winner;
    const currentPlayer = this.state.currentPlayer;

    if (winner === "null") {
      return `Current Player: Player ${currentPlayer} | Game Phase: ${this.state.gamePhase === 1 ? 'Setup' : 'Play'}`;
    } else {
      return `Winner: ${winner} | Game Phase: ${this.state.gamePhase === 1 ? 'Setup' : 'Play'}`;
    }
  }
  createCell(cell: Cell, index: number): React.ReactNode {
    return (
      <div key={index}>
        <a href='/' onClick={this.play(cell.x, cell.y)}>
          <BoardCell cell={cell}></BoardCell>
        </a>
      </div>  
    )
  }

  componentDidMount(): void {
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    // console.log('cells', this.state.cells); //probe cells
    return (
      <div style={{ backgroundImage: `url(${map})`, backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh' }}>
        <div id="instructions">{this.instructions()}</div>
        <div id="board" >
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="bottombar" style={{ display: 'flex', justifyContent: 'space-between' }}>
          <button onClick={this.newGame}>New Game</button>
        </div>
      </div>
    );
    }
}

export default App;