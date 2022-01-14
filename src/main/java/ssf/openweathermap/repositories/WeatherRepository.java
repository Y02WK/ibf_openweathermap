package ssf.openweathermap.repositories;

import static ssf.openweathermap.constants.OWMConstants.REDIS_TEMPLATE;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * WeatherRepository
 */
@Repository
public class WeatherRepository {
    @Autowired
    @Qualifier(REDIS_TEMPLATE)
    private RedisTemplate<String, String> template;

    public Optional<String> get(String key) {
        return Optional.ofNullable(template.opsForValue().get(key));
    }

    public void save(String key, String value) {
        template.opsForValue().set(key, value, 60, TimeUnit.MINUTES);
    }

    public boolean hasKey(String key) {
        return template.hasKey(key);
    }
}