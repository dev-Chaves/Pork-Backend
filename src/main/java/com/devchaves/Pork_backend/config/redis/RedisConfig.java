package com.devchaves.Pork_backend.config.redis;

import com.devchaves.Pork_backend.DTO.DashboardDTO;
import com.devchaves.Pork_backend.DTO.ExpenseListDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserInfoResponse;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    Duration fastCache = Duration.ofMinutes(10);

    Duration longCache = Duration.ofHours(24);

    @Bean
    @Primary
    public ObjectMapper redisObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;

    }

    private <T> RedisCacheConfiguration createCacheConfiguration(ObjectMapper objectMapper,Class<T> clazz, Duration ttl){
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(objectMapper, clazz);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }



    RedisCacheConfiguration stringCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(fastCache)
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {

        Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();


        cacheConfiguration.put("despesa_cache", createCacheConfiguration(objectMapper, ExpenseListDTO.class, fastCache));
        cacheConfiguration.put("receitaCache", createCacheConfiguration(objectMapper, ReceitaResponseDTO.class, fastCache));
        cacheConfiguration.put("userCache", createCacheConfiguration(objectMapper, UserInfoResponse.class, longCache));
        cacheConfiguration.put("userDetailsCache", createCacheConfiguration(objectMapper, UserEntity.class, longCache));

        cacheConfiguration.put("gastos_cache", stringCacheConfig);

        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        return new LoggingRedisCacheManager(
                cacheWriter,
                defaultCacheConfig,
                cacheConfiguration,
                true
        );
    }

}
