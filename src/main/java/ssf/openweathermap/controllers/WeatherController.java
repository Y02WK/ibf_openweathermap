package ssf.openweathermap.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import ssf.openweathermap.models.WeatherDisplayModel;
import ssf.openweathermap.services.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private WeatherService cacheService;

    private final Logger logger = Logger.getLogger(WeatherController.class.getName());

    @PostMapping
    public String processInput(@RequestBody MultiValueMap<String, String> form, Model model) {
        final String cityName = form.getFirst("city").replaceAll(" ", "+");
        WeatherDisplayModel data;
        try {
            data = cacheService.getWeatherDisplayModel(cityName);
            model.addAttribute("main", data);
            model.addAttribute("readings", data.getWeatherList());
            return "weather";
        } catch (IOException e) {
            logger.log(Level.WARNING, "IO ERROR");
            model.addAttribute("flash", "Server error. Please try again");
            return "index";
        } catch (HttpClientErrorException e) {
            logger.log(Level.WARNING, "CITY NOT FOUND");
            model.addAttribute("flash", "Error city not found!");
            return "index";
        }

    }
}
