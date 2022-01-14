package ssf.openweathermap.config;

import static ssf.openweathermap.constants.OWMConstants.REDIS_TEMPLATE;

import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private final Logger logger = Logger.getLogger(RedisConfig.class.getName());

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    // gets Redis password from environment variable REDIS_PW, if not found sets to
    // empty string
    // System.getenv(Constants.REDIS_PASSWORD) = System.getenv("REDIS_PASSWORD");
    // System.getenv("REDIS_PW")
    @Value("#{systemEnvironment['REDIS_PW']}")
    private String redisPassword;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer jedisPoolMaxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer jedisPoolMaxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer jedisPoolMinIdle;

    @Bean(REDIS_TEMPLATE)
    public RedisTemplate<String, String> createRedisTemplate() {
        // configuring the database
        final RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort.get());
        if (redisPassword != null) {
            redisConfig.setPassword(redisPassword);
        }

        // setup poolConfig
        final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(jedisPoolMaxActive);
        poolConfig.setMinIdle(jedisPoolMinIdle);
        poolConfig.setMaxTotal(jedisPoolMaxIdle);

        // create client and factory
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().usePooling()
                .poolConfig(poolConfig).build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(redisConfig, jedisClient);
        jedisFac.afterPropertiesSet();

        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer()); // keys in utf-8
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
