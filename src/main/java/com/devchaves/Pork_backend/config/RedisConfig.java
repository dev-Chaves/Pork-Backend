package com.devchaves.Pork_backend.config;

import com.devchaves.Pork_backend.DTO.DashboardDTO;
import com.devchaves.Pork_backend.DTO.ExpenseListDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public ObjectMapper redisObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;

    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){

        Jackson2JsonRedisSerializer<ExpenseListDTO> expenseListSerializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper(), ExpenseListDTO.class);

        Jackson2JsonRedisSerializer<ReceitaResponseDTO> receitaSerializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper(), ReceitaResponseDTO.class);

        Jackson2JsonRedisSerializer<UserInfoResponse> userInfoSerializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper(), UserInfoResponse.class);

        Jackson2JsonRedisSerializer<DashboardDTO> dashboardSerializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper(), DashboardDTO.class);

        // Cache Despesa -> despesa_cache
        RedisCacheConfiguration despesaCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(expenseListSerializer));

        RedisCacheConfiguration receitaCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(receitaSerializer));

        RedisCacheConfiguration userInfoCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(userInfoSerializer));

        RedisCacheConfiguration dashboardCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(dashboardSerializer));

        Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();

        cacheConfiguration.put("despesa_cache", despesaCacheConfig);
        cacheConfiguration.put("receitaCache", receitaCacheConfig);
        cacheConfiguration.put("userCache", userInfoCacheConfig);
        cacheConfiguration.put("dashboard_cache", dashboardCacheConfig);

        return RedisCacheManager.builder(redisConnectionFactory).withInitialCacheConfigurations(cacheConfiguration).build();

    }

}
