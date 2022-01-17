package ssf.openweathermap.services;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import ssf.openweathermap.interfaces.WeatherService;
import ssf.openweathermap.models.Weather;
import ssf.openweathermap.models.WeatherDisplayModel;

/**
 * WeatherServiceNonCaching
 */
@Service
public class WeatherServiceNonCaching implements WeatherService {
    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    private Logger logger = Logger.getLogger(WeatherServiceNonCaching.class.getName());

    public WeatherDisplayModel getWeatherDisplayModel(String key) throws IOException {
        logger.info("Using WeatherServiceNonCaching for %s".formatted(key));
        JsonObject jsonData = getWeatherJson(key.toUpperCase());
        return convertToModel(jsonData);
    }

    protected JsonObject getWeatherJson(String key) throws HttpClientErrorException, IOException {
        JsonObject data = openWeatherMapService.useExchange(key);
        return data;
    }

    protected WeatherDisplayModel convertToModel(JsonObject jsonObject) {
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