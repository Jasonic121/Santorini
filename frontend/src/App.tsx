/* es-lint-disable */
import React, { useEffect, useState } from 'react';
import { GameState, Cell } from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import map from './resources/img/background/map.png';
import gameOverImage from './resources/img/background/victory.png';

interface Props {}

interface State extends GameState {
  gamePhase: number; // 0 for not started, 1 for setup, 2 for Worker Selection, 3 for Target Cell Selection
  workerPhase: number; // 0 for move, 1 for build
  selectedCells: Cell[];
  selectedWorkerCell: Cell | null;
  isGameOver: boolean;
}

class App extends React.Component<Props, State> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      winner: -1,
      currentPlayer: 0,
      gamePhase: 0,
      workerPhase: 0,
      selectedCells: [],
      selectedWorkerCell: null,
      validCells: [],
      isGameOver: false,
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
        validCells: json['validCells'],
        selectedWorkerCell: null,
        workerPhase: 0,
        isGameOver: false,
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
    const isValidCell = this.state.validCells.find(cell => cell.x === x && cell.y === y);

    if (isValidCell) {
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
          // gamePhase will change to 2 if the previous workerPhase was 1 and the previous gamePhase was 3
          gamePhase: prevState.workerPhase === 1 && prevState.gamePhase === 3 ? 2 : prevState.gamePhase,
          isGameOver: json['winner'] !== -1,
        }));
    } else {
        console.log('Clicked cell is not a valid move or build location');
    }
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
        return 'Moving... (Choose Worker)';
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

    if (winner === -1) {
      return `Current Player: Player ${currentPlayer} | Game Phase: ${gamePhaseString}`;
    } else {
      return `Winner: Player ${winner}`;
    }
  };

  createCell = (cell: Cell, index: number): React.ReactNode => {
    const onClick = (e: React.MouseEvent) => {
      e.preventDefault();
      console.log("gamePhase: ", this.state.gamePhase, "workerPhase: ", this.state.workerPhase);
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
        {this.state.isGameOver && (
          <div style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <img src={gameOverImage} alt="Game Over" style={{ maxWidth: '100%', maxHeight: '100%' }} />
          </div>
        )}
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