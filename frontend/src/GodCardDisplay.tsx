import React from 'react';

// Assuming these imports are correct and the file paths are accurate
import demeterCard from './resources/img/cards/demeter/card.png';
import hephaestusCard from './resources/img/cards/hephaestus/card.png';
import minotaurCard from './resources/img/cards/minotaur/card.png';
import panCard from './resources/img/cards/pan/card.png';

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
  const renderGodCard = (card: GodCardInterface) => (
    <div
      className={`god-card ${selectedGodCards.some(sc => sc.name === card.name) ? 'selected' : ''}`}
      onClick={() => handleGodCardSelection && handleGodCardSelection(card)}
    >
      <img src={card.image} alt={card.name} />
    </div>
  );

  if (displayOnlySelected && currentPlayer !== undefined) {
    return (
      <div className="god-card-selection">
        {selectedGodCards.filter(card => card.name === (currentPlayer === 0 ? selectedGodCards[0].name : selectedGodCards[1].name)).map(card => renderGodCard(card))}
      </div>
    );
  }

  return (
    <div className="god-card-selection">
      {renderGodCard({ name: 'Demeter', image: demeterCard })}
      {renderGodCard({ name: 'Hephaestus', image: hephaestusCard })}
      {renderGodCard({ name: 'Minotaur', image: minotaurCard })}
      {renderGodCard({ name: 'Pan', image: panCard })}
    </div>
  );
};

export default GodCardDisplay;
