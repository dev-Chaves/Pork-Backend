package com.devchaves.Pork_backend.config.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

public class LoggingRedisCacheManager extends RedisCacheManager {

    // Este é o construtor que resolve o primeiro erro.
    // Ele aceita os argumentos e os passa para a classe "mãe" usando super().
    public LoggingRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    // Este método está correto e cria a nossa instância de cache com log.
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new LoggingRedisCache(name, getCacheWriter(), cacheConfig != null ? cacheConfig : getDefaultCacheConfiguration());
    }
}
