package ssf.openweathermap.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import ssf.openweathermap.models.Weather;
import ssf.openweathermap.models.WeatherDisplayModel;
import ssf.openweathermap.repositories.WeatherRepository;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    // private Logger logger = Logger.getLogger(CacheService.class.getName());

    public WeatherDisplayModel getWeatherDisplayModel(String key) throws IOException {
        JsonObject jsonData = getWeatherJson(key.toUpperCase());
        return convertToModel(jsonData);
    }

    // checks if cache contains the data requested
    private boolean checkCache(String key) {
        return weatherRepository.hasKey(key);
    }

    private JsonObject getWeatherJson(String key) throws IOException {
        if (checkCache(key)) {
            String weatherData = weatherRepository.get(key).get();
            JsonReader reader = Json.createReader(new StringReader(weatherData));
            return reader.readObject();
        } else {
            JsonObject data = openWeatherMapService.useExchange(key);
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stringWriter);
            jsonWriter.writeObject(data);
            weatherRepository.save(key, stringWriter.toString());
            return data;
        }
    }

    private WeatherDisplayModel convertToModel(JsonObject jsonObject) {
        WeatherDisplayModel weatherDisplayModel = new WeatherDisplayModel();
        weatherDisplayModel.setCity(jsonObject.getString("name"));
        weatherDisplayModel.setTemperature(jsonObject.getJsonObject("main").get("temp").toString());
        JsonArray weatherReadings = jsonObject.getJsonArray("weather");
        weatherDisplayModel.setWeatherList(
                weatherReadings.stream()
                        .map(v -> (JsonObject) v)
                        .map(Weather::create)
                        .map(w -> w)
                        .collect(Collectors.toList()));
        return weatherDisplayModel;
    }
}
