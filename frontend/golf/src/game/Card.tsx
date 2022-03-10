import React, {forwardRef} from "react";

export const CARD_WIDTH = 60;
export const CARD_HEIGHT = 84;

function cardPath(name: string): string {
  return `img/cards/${name}.svg`;
}

interface CardProps {
  className: string;
  name: string;
  x: number;
  y: number;
}

export const Card = forwardRef<SVGImageElement, CardProps>(
  (props, ref) => {
    const className = props.className || "Card";
    const href = cardPath(props.name);
    const width = "10%";
    const x = props.x - CARD_WIDTH / 2;
    const y = props.y - CARD_HEIGHT / 2;

    return (
      <image
        className={className}
        ref={ref}
        href={href}
        width={width}
        x={x}
        y={y}
      />
    );
  });