import React, {forwardRef} from "react";

export const CARD_WIDTH = 60;
export const CARD_HEIGHT = 84;

function cardPath(name: string): string {
  return `img/cards/${name}.svg`;
}

interface CardProps {
  cardName: string;
  x: number;
  y: number;
  className?: string;
  onClick?: () => any;
}

export const Card = forwardRef<SVGImageElement, CardProps>(
  (props, ref) => {
    const href = cardPath(props.cardName);
    const width = "10%";
    const x = props.x - CARD_WIDTH / 2;
    const y = props.y - CARD_HEIGHT / 2;

    let className = "Card";
    if (props.className) {
      className += " " + props.className;
    }

    return (
      <image
        className={className}
        ref={ref}
        href={href}
        width={width}
        x={x}
        y={y}
        onClick={props.onClick}
      />
    );
  });
