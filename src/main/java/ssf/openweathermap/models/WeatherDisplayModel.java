package ssf.openweathermap.models;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class WeatherDisplayModel {
    private String city;
    private String temperature;
    private List<Weather> weatherList;

    public String getCity() {
        return this.city;
    }

    public List<Weather> getWeatherList() {
        return this.weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public JsonObject toJson() {
        JsonArrayBuilder weatherArray = Json.createArrayBuilder();

        for (Weather w : this.weatherList) {
            weatherArray.add(w.toJsonObject());
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("city", this.city)
                .add("temperature", this.temperature)
                .add("weatherList", weatherArray)
                .build();

        return jsonObject;
    }
}
