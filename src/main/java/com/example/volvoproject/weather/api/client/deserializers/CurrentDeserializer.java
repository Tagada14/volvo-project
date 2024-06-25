package com.example.volvoproject.weather.api.client.deserializers;

import com.example.volvoproject.weather.api.client.Dto.Current;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CurrentDeserializer extends StdDeserializer<Current> {

    public CurrentDeserializer() {
        this(null);
    }

    public CurrentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Current deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(node.get("last_updated_epoch").asLong()),
                ZoneId.systemDefault());
        return new Current(date);
    }
}
