/* es-lint-disable */
import React from 'react';
import { GameState, Cell } from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import GodCardDisplay from './GodCardDisplay.tsx';

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
  secondBuild: boolean;
}

class App extends React.Component<Props, State> {
  private initialized: boolean = false;
  private testing: boolean = false;
  constructor(props: Props) {
    super(props);
    this.state = {
      secondBuild: false,
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
        gamePhase: json['gamePhase'],
        workerPhase: json['workerPhase'],
        cells: json['cells'],
        selectedCells: [],
        winner: json['winner'],
        currentPlayer: json['currentPlayer'],
        validCells: json['validCells'],
        selectedWorkerCell: null,
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
    const { currentPlayer, selectedGodCards } = this.state;
    const newSelection = [...selectedGodCards, selectedCard];
  
    if (currentPlayer === 0) {
      this.setState({ player1GodCard: selectedCard });
    } else {
      this.setState({ player2GodCard: selectedCard });
    }

    console.log('Player', currentPlayer + 1, 'selected', selectedCard.name);
  
    this.setState({ selectedGodCards: newSelection });
    const response = await fetch(`http://localhost:8080/godCardSelection?player=${currentPlayer}&card=${selectedCard.name}`);
    const json = await response.json();

    this.setState({
      cells: json['cells'],
      winner: json['winner'],
      currentPlayer: json['currentPlayer'], 
          });
    
      if (newSelection.length >= 2) {
      this.setState({
        gamePhase: 1,
        showGodCardSelection: false,
      });
    } else {
      this.setState({
        currentPlayer: currentPlayer === 0 ? 1 : 0,
        gamePhase: 0, 
      });
    }
  };
  
  

  handleWorkerSelection = async (clickedCell: Cell | undefined, x: number, y: number) => {
    const response = await fetch(`http://localhost:8080/selectedWorker?workerphase=${this.state.workerPhase}&x=${x}&y=${y}`);
    const json = await response.json();
    this.setState({
      cells: json['cells'],
      winner: json['winner'],
      currentPlayer: json['currentPlayer'],
      gamePhase: json['gamePhase'],
      workerPhase: json['workerPhase'],
    });

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

      if (json['gamePhase'] === 3 && json['workerPhase'] === 1) {
        // If there are remaining build points, allow the player to choose another cell for the second build
        this.setState((prevState) => ({
          cells: json['cells'],
          winner: json['winner'],
          currentPlayer: json['currentPlayer'],
          selectedWorkerCell: null,
          gamePhase: 3,
          workerPhase: 1,
          validCells: json['validCells'],
          isGameOver: json['winner'] !== -1,
          secondBuild: json['secondBuild'],
        }));
        console.log("Valid cells: ", this.state.validCells);
      } else {
        // If there are no remaining build points, proceed to the next player's move action
        this.setState((prevState) => ({
          cells: json['cells'],
          winner: json['winner'],
          currentPlayer: json['currentPlayer'],
          selectedWorkerCell: null,
          gamePhase: json['gamePhase'],
          workerPhase: json['workerPhase'],
          validCells: json['validCells'],
          isGameOver: json['winner'] !== -1,
          secondBuild: json['secondBuild'],
        }));
      }
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
      gamePhase: json['gamePhase'],
      workerPhase: json['workerPhase'],
    });
  };

  getGamePhaseString = (): string => {
    const { gamePhase, workerPhase } = this.state;
    switch (gamePhase) {
      case 0:
        return 'God Card Selection';
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

  instructions = (): React.ReactNode => {
    const { winner, currentPlayer, gamePhase, showGodCardSelection, player1GodCard, player2GodCard } = this.state;
  
    // Determine the game phase for message display
    let instructionText;
    if (winner !== -1) {
      instructionText = (
        <div className='end-game'>
          PLAYER {winner + 1} WINS! Press New Game to Start.
        </div>
      );
    } else if (showGodCardSelection) {
      instructionText = (
        <div className='instruction-text'>
          Player {currentPlayer + 1}, select your god card.{"     "}
        </div>
      );
    } else {
      const gamePhaseString = this.getGamePhaseString();
      instructionText = (
        <div className='instruction-text'>
          Player {currentPlayer + 1}'s Turn - {gamePhaseString}
        </div>
      );
    }
  
    return (
      <div className='in-game'>{instructionText}</div>
    );
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
      gamePhase: json['gamePhase'],
      workerPhase: json['workerPhase'],
      validCells: json['validCells'],
    });
  };
  render(): React.ReactNode {
    const { selectedGodCards, gamePhase, currentPlayer, player1GodCard, player2GodCard } = this.state;
    console.log('Game phase:', gamePhase, 'workerPhase:', this.state.workerPhase);
    const isSecondBuild = this.state.secondBuild;
    const player1Class = `player1-gods-display ${currentPlayer === 0 ? 'current-player' : 'not-current-player'}`;
    const player2Class = `player2-gods-display ${currentPlayer === 1 ? 'current-player' : 'not-current-player'}`;

    return (
      <div className="app">
        {this.state.showGodCardSelection ? (
          <div>
            <div id="instructions">{this.instructions()}</div>
            <GodCardDisplay
              selectedGodCards={selectedGodCards}
              handleGodCardSelection={this.handleGodCardSelection}
            />
          </div>
        ) : (
          <>
            <div id="instructions">{this.instructions()}</div>
            <div id="board">
              {this.state.cells.map((cell, i) => this.createCell(cell, i))}
            </div>
            <div>
              <div className={player1Class}>
                <GodCardDisplay
                  selectedGodCards={[player1GodCard].filter(Boolean)}
                  displayOnlySelected={true}
                  currentPlayer={currentPlayer}
                />
              </div>
              <div className={player2Class}>
                <GodCardDisplay
                  selectedGodCards={[player2GodCard].filter(Boolean)}
                  displayOnlySelected={true}
                  currentPlayer={currentPlayer}
                />
              </div>
            </div>
            <div id="bottombar" className='bottom-bar'>
              <button onClick={this.newGame} className='new-game'>New Game</button>
              <button onClick={() => this.sendTestLayout(this.testLayout)} className='test-layout'>Test Layout</button>
              {this.state.secondBuild && (
              <button onClick={this.pass} className='pass'>Pass Additional Build</button>
              )}
            </div>
          </>
        )}
      </div>
    );
  }
}

export default App;