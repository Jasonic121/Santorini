/* es-lint-disable */
import React from 'react';
import { GameState, Cell } from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import demeterCard from './resources/img/cards/demeter/card.png';
import hephaestusCard from './resources/img/cards/hephaestus/card.png';
import minotaurCard from './resources/img/cards/minotaur/card.png';
import panCard from './resources/img/cards/pan/card.png';

interface Props {}

interface GodCardInterface {
  name: string;
  image: string;
}
interface State extends GameState {
  gamePhase: number; // 0 for god card selection, 1 for setup, 2 for Worker Selection, 3 for Target Cell Selection, 4 for Game Over
  workerPhase: number; // 0 for move, 1 for build
  selectedCells: Cell[];
  selectedWorkerCell: Cell | null;
  isGameOver: boolean;
  showGodCardSelection: boolean;
  selectedGodCards: GodCardInterface[];
  player1GodCard: GodCardInterface | null;
  player2GodCard: GodCardInterface | null;
}

class App extends React.Component<Props, State> {
  private initialized: boolean = false;
  private testing: boolean = false;
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
      showGodCardSelection: true,
      selectedGodCards: [],
    player1GodCard: null,
    player2GodCard: null,
    };
  }

  newGame = async () => {
    try {
      const response = await fetch('http://localhost:8080/newgame');
      const json = await response.json();
      this.setState({
        gamePhase: 0,
        cells: json['cells'],
        selectedCells: [],
        winner: json['winner'],
        currentPlayer: json['currentPlayer'],
        validCells: json['validCells'],
        selectedWorkerCell: null,
        workerPhase: 0,
        isGameOver: false,
        showGodCardSelection: true,
        selectedGodCards: [],
      });
      console.log('cells', this.state.cells);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  };

  handleGodCardSelection = async (selectedCard: GodCardInterface) => {
    const { selectedGodCards } = this.state;
    const playerIndex = selectedGodCards.length;

    if (playerIndex < 2) {
      const updatedSelection = [...selectedGodCards, selectedCard];
      this.setState({ selectedGodCards: updatedSelection });

      if (updatedSelection.length === 2) {
        const player1GodCard = updatedSelection[0].name;
        const player2GodCard = updatedSelection[1].name;

        const response = await fetch(`http://localhost:8080/godCardSelection?player1GodCard=${player1GodCard}&player2GodCard=${player2GodCard}`);
        const json = await response.json();
        this.setState({
          cells: json['cells'],
          winner: json['winner'],
          currentPlayer: json['currentPlayer'],
          gamePhase: 1,
          showGodCardSelection: false,
        });
      }
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
      this.setState({ gamePhase: 3, selectedWorkerCell: clickedCell, validCells: json['validCells'] });
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
        gamePhase: prevState.workerPhase === 1 && prevState.gamePhase === 3 ? 2 : prevState.gamePhase,
        isGameOver: json['winner'] !== -1,
      }));
    } else {
      console.log('Clicked cell is not a valid move or build location');
    }
  };

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
      case 4:
        return 'Game Over';
      default:
        return 'Press New Game to Start';
    }
  };

  instructions = (): string => {
    const winner = this.state.winner;
    const currentPlayer = +this.state.currentPlayer + 1;
    const gamePhaseString = this.getGamePhaseString();

    if (winner === -1) {
      return (
        <div className='in-game'>
          <div className='instruction-text'>
            Player {currentPlayer}'s Turn     {gamePhaseString}
          </div>
        </div>
      );
    } else {
      return (
        <div className='in-game'>
          <div className='instruction-text'>
            Thank you for playing!     Press New Game to Start
          </div>
          <div className='end-game'>
            PLAYER {+winner + 1} WINS!
          </div>
        </div>
      );
    }
  };

  createCell = (cell: Cell, index: number): React.ReactNode => {
    const onClick = (e: React.MouseEvent) => {
      e.preventDefault();
      if (this.state.isGameOver) {
        console.log('Game is over. Please start a new game.');
        this.setState({ validCells: [] });
        return;
      }
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
          <BoardCell
            cell={cell}
            selectedWorkerCell={this.state.selectedWorkerCell}
            validCells={this.state.validCells}
            isCurrentPlayerWorker={Number(cell.occupiedBy) === this.state.currentPlayer}
            currentPlayer={this.state.currentPlayer}
            currentGamePhase={this.state.gamePhase}
            currentWorkerPhase={this.state.workerPhase}
          ></BoardCell>
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

  testLayout =
    `1,0,-1;0,0,-1;2,0,1;0,0,-1;0,0,1;` + // row 1
    `1,0,-1;0,0,-1;0,0,-1;2,0,0;3,0,-1;` + // row 2
    `1,0,-1;0,0,-1;0,0,-1;2,0,0;3,0,-1;` + // row 3
    `1,0,-1;0,0,-1;0,0,-1;3,0,-1;0,0,-1;` + // row 4
    `1,0,-1;0,0,-1;0,0,-1;2,0,-1;0,0,-1;` + // row 5
    `0`; // 0 is the current player

  sendTestLayout = async (layout: string) => {
    try {
      const response = await fetch(`http://localhost:8080/testLayout?layout=${layout}`);
      const json = await response.json();
      this.setState({
        cells: json['cells'],
        winner: json['winner'],
        currentPlayer: json['currentPlayer'],
        gamePhase: 2,
        workerPhase: 0,
        validCells: json['validCells'],
      });
      console.log("state", this.state);
      this.testing = true;
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  };

  pass = async () => {
    const response = await fetch('http://localhost:8080/pass');
    const json = await response.json();
    this.setState({
      cells: json['cells'],
      winner: json['winner'],
      currentPlayer: json['currentPlayer'],
      gamePhase: 2,
      workerPhase: 0,
      validCells: json['validCells'],
    });
  };

  render(): React.ReactNode {
    const selectedGodCards = this.state.selectedGodCards;

    return (
      <div className="app">
        {this.state.showGodCardSelection ? (
          <div className="god-card-selection">
            <div
              className={`god-card ${selectedGodCards.some(card => card.name === 'Demeter') ? 'selected' : ''}`}
              onClick={() => this.handleGodCardSelection({ name: 'Demeter', image: demeterCard })}
            >
              <img src={demeterCard} alt="Demeter" />
              <span>Demeter</span>
            </div>
            <div
              className={`god-card ${selectedGodCards.some(card => card.name === 'Hephaestus') ? 'selected' : ''}`}
              onClick={() => this.handleGodCardSelection({ name: 'Hephaestus', image: hephaestusCard })}
            >
              <img src={hephaestusCard} alt="Hephaestus" />
              <span>Hephaestus</span>
            </div>
            <div
              className={`god-card ${selectedGodCards.some(card => card.name === 'Minotaur') ? 'selected' : ''}`}
              onClick={() => this.handleGodCardSelection({ name: 'Minotaur', image: minotaurCard })}
            >
              <img src={minotaurCard} alt="Minotaur" />
              <span>Minotaur</span>
            </div>
            <div
              className={`god-card ${selectedGodCards.some(card => card.name === 'Pan') ? 'selected' : ''}`}
              onClick={() => this.handleGodCardSelection({ name: 'Pan', image: panCard })}
            >
              <img src={panCard} alt="Pan" />
              <span>Pan</span>
            </div>
          </div>
        ) : (
          <>
            <div id="instructions">{this.instructions()}</div>
            <div id="board">
              {this.state.cells.map((cell, i) => this.createCell(cell, i))}
            </div>
            <div id="bottombar" className='bottom-bar'>
              <button onClick={this.newGame} className='new-game'>New Game</button>
              <button onClick={() => this.sendTestLayout(this.testLayout)} className='test-layout'>Test Layout</button>
              <button onClick={this.pass} className='pass'>Pass</button>
            </div>
          </>
        )}
      </div>
    );
  }
}

export default App;