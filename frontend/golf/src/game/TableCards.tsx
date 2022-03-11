import {Card} from "./Card";
import {TABLE_CARD_COORD} from "./coord";

interface TableCardsProps {
  tableCards: string[];
}

export function TableCards(props: TableCardsProps) {
  const {tableCards} = props;
  const {x, y} = TABLE_CARD_COORD;
  const name = tableCards[0];
  // const secondCard = tableCards[1];

  return (
    <>
      <Card
        className="TableCards"
        cardName={name}
        x={x}
        y={y}
      />
    </>
  );
}
