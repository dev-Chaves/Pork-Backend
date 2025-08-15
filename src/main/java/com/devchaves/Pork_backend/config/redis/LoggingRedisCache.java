package com.devchaves.Pork_backend.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class LoggingRedisCache extends RedisCache {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRedisCache.class);

    protected LoggingRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfiguration) {
        super(name, cacheWriter, cacheConfiguration);
    }

    @Override
    protected Object lookup(Object key) {

        Object value = super.lookup(key);

        if (value != null){
            logger.info("CACHE HIT! Chave: '{}' encontrada no cache '{}'", key, getName());
        }else {
            logger.info("Não HITOU!");
        }

        return value;
    }
}
