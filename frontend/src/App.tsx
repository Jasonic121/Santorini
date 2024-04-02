/* es-lint-disable */
import React, { useEffect, useState } from 'react';
import { GameState, Cell} from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import map from './resources/img/background/map.png';

interface Props {}
interface State extends GameState {
  startingGame: boolean;
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
      startingGame: false,
      selectedCells: [],
    };
  }

  newGame = async () => {
    try {
      const response = await fetch('http://localhost:8080/newgame');
      const json = await response.json();
      this.setState({
        startingGame: true,
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

      if (this.state.startingGame) {
        const clickedCell = this.state.cells.find((cell) => cell.x === x && cell.y === y);
        if (clickedCell && !this.state.selectedCells.some(cell => cell.x === clickedCell.x && cell.y === clickedCell.y)) {
          this.setState((prevState) => ({
            selectedCells: [...prevState.selectedCells, clickedCell],
          }), () => {
            if (this.state.selectedCells.length === 4) {
              this.setupInitialWorkers();
            }
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

  setupInitialWorkers = async () => {
    const [cell1, cell2, cell3, cell4] = this.state.selectedCells;
    const response = await fetch(`http://localhost:8080/setup?cell1=${cell1.x},${cell1.y}&cell2=${cell2.x},${cell2.y}&cell3=${cell3.x},${cell3.y}&cell4=${cell4.x},${cell4.y}`);
    const json = await response.json();
    this.setState({
      cells: json['cells'],
      winner: json['winner'],
      currentPlayer: json['currentPlayer'],
      startingGame: false,
      selectedCells: [],
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

    if (winner === "null")
      return  `Current Player: ${currentPlayer}`;
    else 
      return `Winner: ${winner}`;
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