import React from 'react';

// Assuming these imports are correct and the file paths are accurate
import demeterCard from './resources/img/cards/demeter/card.png';
import hephaestusCard from './resources/img/cards/hephaestus/card.png';
import minotaurCard from './resources/img/cards/minotaur/card.png';
import panCard from './resources/img/cards/pan/card.png';
import normal from './resources/img/cards/retro.jpg';

interface GodCardInterface {
  name: string;
  image: string;
}

interface Props {
  selectedGodCards: GodCardInterface[];
  handleGodCardSelection?: (card: GodCardInterface) => void;
  displayOnlySelected?: boolean;
  currentPlayer?: number; // Optional, only needed if displayOnlySelected is true
}

const GodCardDisplay: React.FC<Props> = ({ selectedGodCards, handleGodCardSelection, displayOnlySelected, currentPlayer }) => {
  const renderGodCard = (card: GodCardInterface, isCurrentPlayer: boolean) => (
    <div
      className={`god-card ${selectedGodCards.some(sc => sc.name === card.name) ? 'selected' : ''} ${isCurrentPlayer ? 'current-player' : 'other-player'}`}
      onClick={() => handleGodCardSelection && handleGodCardSelection(card)}
    >
      <img src={card.image} alt={card.name} />
    </div>
  );

  if (displayOnlySelected && currentPlayer !== undefined) {
    return (
      <div className="god-card-selection">
        {selectedGodCards.map((card, index) => renderGodCard(card, index === currentPlayer))}
      </div>
    );
  }

  return (
    <div className="god-card-selection">
      {renderGodCard({ name: 'Demeter', image: demeterCard }, false)}
      {renderGodCard({ name: 'Hephaestus', image: hephaestusCard }, false)}
      {renderGodCard({ name: 'Minotaur', image: minotaurCard }, false)}
      {renderGodCard({ name: 'Pan', image: panCard }, false)}
      {renderGodCard({ name: 'Normal', image: normal }, false)}
    </div>
  );
};

export default GodCardDisplay;