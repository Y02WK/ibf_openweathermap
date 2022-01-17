package ssf.openweathermap.interfaces;

import java.io.IOException;

import ssf.openweathermap.models.WeatherDisplayModel;

public interface WeatherService {
    public WeatherDisplayModel getWeatherDisplayModel(String key) throws IOException;
}
