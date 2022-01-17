package ssf.openweathermap.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import ssf.openweathermap.interfaces.WeatherService;
import ssf.openweathermap.models.WeatherDisplayModel;

@RestController
@RequestMapping("/weather")
public class WeatherJsonController {
    @Autowired
    @Qualifier("CACHING_WEATHER_SERVICE")
    private WeatherService weatherService;

    @GetMapping("{city}")
    public ResponseEntity<String> getWeather(@PathVariable("city") String city) {
        final String cityName = city.replaceAll(" ", "+");
        WeatherDisplayModel data;
        JsonObject respObject;
        try {
            data = weatherService.getWeatherDisplayModel(cityName);
            respObject = data.toJson();
            return ResponseEntity.ok(respObject.toString());
        } catch (IOException e1) {
            respObject = Json.createObjectBuilder()
                    .add("error", "Error occurred while retreving weather data.")
                    .build();
            return ResponseEntity.internalServerError().body(respObject.toString());
        } catch (HttpClientErrorException e2) {
            respObject = Json.createObjectBuilder()
                    .add("error", "City not found!")
                    .build();
            return ResponseEntity.badRequest().body(respObject.toString());
        }
    }
}
