package com.example.volvoproject.weather.api.client.deserializers;

import com.example.volvoproject.weather.api.client.Dto.Forecast;
import com.example.volvoproject.weather.api.client.Dto.ForecastDay;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ForecastDeserializer extends StdDeserializer<Forecast> {
    private static final Logger LOG = LoggerFactory.getLogger(ForecastDeserializer.class);

    public ForecastDeserializer() {
        this(null);
    }

    public ForecastDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Forecast deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        try {
            JsonNode node = jp.getCodec().readTree(jp);
            node = node.get("forecastday");
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            List<ForecastDay> forecastDays = mapper.readerForListOf(ForecastDay.class).readValue(node);
            return new Forecast(forecastDays);
        } catch (JsonProcessingException e) {
            LOG.error("Error while trying to parse JSON to Forecast", e);
            throw new IOException(e.getMessage());
        }
    }
}
