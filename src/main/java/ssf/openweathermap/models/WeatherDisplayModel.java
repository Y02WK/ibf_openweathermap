package ssf.openweathermap.models;

import java.util.List;

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

}
