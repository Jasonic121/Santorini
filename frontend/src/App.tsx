/* es-lint-disable */
import React, { useEffect, useState } from 'react';
import { GameState, Cell } from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import map from './resources/img/background/map.png';

interface Props {}

interface State extends GameState {
  gamePhase: number; // 0 for not started, 1 for setup, 2 for Worker Selection, 3 for Target Cell Selection
  workerPhase: number; // 0 for move, 1 for build
  selectedCells: Cell[];
  selectedWorkerCell: Cell | null;
}

class App extends React.Component<Props, State> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      winner: null,
      currentPlayer: 0,
      gamePhase: 0,
      workerPhase: 0,
      selectedCells: [],
      selectedWorkerCell: null,
      validCells: [],
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

  handleWorkerSelection = async (clickedCell: Cell | undefined, x: number, y: number) => {
    const response = await fetch(`http://localhost:8080/selectedWorker?workerphase=${this.state.workerPhase}&x=${x}&y=${y}`);
    const json = await response.json();
    this.setState({ cells: json['cells'], winner: json['winner'], currentPlayer: json['currentPlayer'] });

    console.log('Selected worker:', clickedCell);
    console.log('Current player:', this.state.currentPlayer);

    if (clickedCell && clickedCell.occupied && Number(clickedCell.occupiedBy) === Number(this.state.currentPlayer)) {
      console.log('Worker belongs to current player! Now choose a target cell...');
      this.setState({ gamePhase: 3, selectedWorkerCell: clickedCell, validCells: json['validCells']});
    } else {
      console.log('Worker does not belong to current player');
    }
  };


  handleSelectTargetCell = async (clickedCell: Cell | undefined, x: number, y: number) => {
    const response = await fetch(`http://localhost:8080/selectedTargetCell?workerphase=${this.state.workerPhase}&x=${x}&y=${y}`);
    console.log('Selected target cell:', clickedCell);
    const json = await response.json();
    this.setState((prevState) => ({ 
      cells: json['cells'], 
      winner: json['winner'], 
      currentPlayer: json['currentPlayer'], 
      selectedWorkerCell: null,
      workerPhase: prevState.workerPhase === 0 ? 1 : 0,
      validCells: json['validCells'],
    }));
    console.log("Worker phase changed!");
  }

  handleSetupPhase = (clickedCell: Cell | undefined) => {
    if (clickedCell && !this.state.selectedCells.some(cell => cell.x === clickedCell.x && cell.y === clickedCell.y)) {
      this.setState((prevState) => ({
        selectedCells: [...prevState.selectedCells, clickedCell],
      }), () => {
        this.setupInitialWorker(clickedCell);
      });
    }
    console.log('Selected cells:', this.state.selectedCells);
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
          gamePhase: 2, // Change to moving phase
          selectedCells: [],
        });
        console.log('Game phase changed to:', 2);
      }
    });
  };

  getGamePhaseString = (): string => {
    const { gamePhase, workerPhase } = this.state;
    switch (gamePhase) {
      case 1:
        return 'Setup';
      case 2:
        return workerPhase === 0 ? 'Moving... (Choose Worker)' : 'Building... (Choose Worker)';
      case 3:
        return workerPhase === 0 ? 'Moving... (Choose Cell)' : 'Building... (Choose Cell)';
      default:
        return 'Unknown';
    }
  };

  instructions = (): string => {
    const winner = this.state.winner;
    const currentPlayer = +this.state.currentPlayer + 1;
    const gamePhaseString = this.getGamePhaseString();

    if (winner === "null") {
      return `Current Player: Player ${currentPlayer} | Game Phase: ${gamePhaseString}`;
    } else {
      return `Winner: ${winner} | Game Phase: ${gamePhaseString}`;
    }
  };

  createCell = (cell: Cell, index: number): React.ReactNode => {
    const onClick = (e: React.MouseEvent) => {
      e.preventDefault();
      switch (this.state.gamePhase) {
        case 1: // Setup Phase
          this.handleSetupPhase(cell);
          break;
        case 2: // Worker Selection
          this.handleWorkerSelection(cell, cell.x, cell.y);
          break;
        case 3: // Target Cell Selection
          this.handleSelectTargetCell(cell, cell.x, cell.y);
          break;
        default:
          break;
      }
    };
  
    return (
      <div key={index}>
        <a href='/' onClick={onClick}>
          <BoardCell cell={cell} selectedWorkerCell={this.state.selectedWorkerCell} validCells={this.state.validCells}></BoardCell>
        </a>
      </div>
    );
  };

  componentDidMount(): void {
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    return (
      <div style={{ backgroundImage: `url(${map})`, backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh' }}>
        <div id="instructions">{this.instructions()}</div>
        <div id="board">
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