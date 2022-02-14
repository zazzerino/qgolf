package com.kdp.golf.game.model.card;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

class CardSerializer extends StdSerializer<Card> {

    public CardSerializer() {
        this(null);
    }

    public CardSerializer(Class<Card> t) {
        super(t);
    }

    @Override
    public void serialize(Card card, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(card.name());
    }
}
