package ssf.openweathermap.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import ssf.openweathermap.interfaces.WeatherService;
import ssf.openweathermap.models.WeatherDisplayModel;
import ssf.openweathermap.repositories.WeatherRepository;

@Service("CACHING_WEATHER_SERVICE")
public class WeatherServiceCaching implements WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherServiceNonCaching delegateWS;

    private Logger logger = Logger.getLogger(WeatherServiceCaching.class.getName());

    public WeatherDisplayModel getWeatherDisplayModel(String key) throws IOException {
        logger.info("Using WeatherServiceCaching for %s".formatted(key));
        JsonObject jsonData = getWeatherJson(key.toUpperCase());
        return delegateWS.convertToModel(jsonData);
    }

    private JsonObject getWeatherJson(String key) throws IOException {
        if (weatherRepository.hasKey(key)) {
            return readFromCache(key);
        } else {
            JsonObject data = delegateWS.getWeatherJson(key);
            writeToCache(key, data);
            return data;
        }
    }

    private JsonObject readFromCache(String key) {
        String weatherData = weatherRepository.get(key).get();
        JsonReader reader = Json.createReader(new StringReader(weatherData));
        return reader.readObject();
    }

    private void writeToCache(String key, JsonObject data) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(data);
        weatherRepository.save(key, stringWriter.toString());
    }
}
