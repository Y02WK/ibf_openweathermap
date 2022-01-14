package ssf.openweathermap.models;

import java.util.logging.Logger;

import jakarta.json.JsonObject;

/**
 * Weather
 */
public class Weather {

    private String id;
    private String main;
    private String description;
    private String icon;

    private static final Logger logger = Logger.getLogger(Weather.class.getName());

    public static Weather create(JsonObject jsonObject) {
        final Weather weather = new Weather();
        weather.setId(jsonObject.get("id").toString());
        weather.setMain(jsonObject.getString("main"));
        weather.setDescription(jsonObject.getString("description"));
        weather.setIcon(jsonObject.getString("icon"));

        logger.info(weather.getMain());
        return weather;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return this.main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = "http://openweathermap.org/img/wn/%s@2x.png".formatted(icon);
    }

}