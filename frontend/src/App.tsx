/* es-lint-disable */
import React, { useEffect, useState } from 'react';
import { GameState, Cell} from './Game.tsx';
import BoardCell from './Cell.tsx';
import './App.css';
import map from './resources/img/background/map.png';

interface Props {}


class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      winner: null,
      currentPlayer: 'Player 1',
    };
  }

  newGame = async () => {
    try {
      const response = await fetch('http://localhost:8080/newgame');
      const json = await response.json();
      this.setState({
        cells: json['cells'],
        winner: json['winner'],
        currentPlayer: json['currentPlayer'],
      });
      console.log('cells', this.state.cells);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  };

  play(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/play?x=${x}&y=${y}`)
      const json = await response.json();
      this.setState({ cells: json['cells'], winner: json['winner'], currentPlayer: json['currentPlayer']});
    }
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
      <BoardCell cell={cell} onClick={() => this.play(cell.x, cell.y)} />
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