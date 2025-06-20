//package com.ad.dena_paona.config.redis;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.time.Duration;
//
//@Configuration
//@EnableConfigurationProperties(ClusterConfigProperties.class)
//@RequiredArgsConstructor
//public class RedisClusterConfig {
//
//    private final ObjectMapper objectMapper;
//
//    private final ClusterConfigProperties clusterConfigProperties;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterConfigProperties.getNodes());
//        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration
//                .builder()
//                .connectTimeout(Duration.ofMillis(12 * 1000))
//                .usePooling().poolConfig(poolConfig())
//                .build();
//        return new JedisConnectionFactory(redisClusterConfiguration, jedisClientConfiguration);
//    }
//
//    @Bean
//    public JedisPoolConfig poolConfig(){
//        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxTotal(1000);
//        poolConfig().setMaxIdle(500);
//        poolConfig().setMinIdle(16);
//        poolConfig().setMaxWaitMillis(15000);
//        poolConfig().setTestOnBorrow(false);
//        poolConfig().setTestOnReturn(false);
//        poolConfig().setTestWhileIdle(false);
//        return poolConfig();
//    }
//
//    @Bean
//    @Primary
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return redisTemplate;
//
//    }
//
//    public <T> Jackson2JsonRedisSerializer<T> configureJackson2JsonRedisSerializer(Class<T> t) {
//        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(t);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        return jackson2JsonRedisSerializer;
//    }
//
//}
