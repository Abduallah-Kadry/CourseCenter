package com.app.coursecenter.config;

import com.app.coursecenter.dto.CourseRatedEvent;
import com.app.coursecenter.dto.CourseReservationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    // ===========================================================
    // ===============  COMMON CONFIGURATION  =====================
    // ===========================================================

    private Map<String, Object> commonProducerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        config.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        return config;
    }

    private Map<String, Object> commonConsumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return config;
    }

    // ===========================================================
    // ===============  COURSE RESERVATION CONFIG  ================
    // ===========================================================

    @Bean
    public ProducerFactory<String, CourseReservationEvent> reservationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(commonProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, CourseReservationEvent> reservationKafkaTemplate() {
        return new KafkaTemplate<>(reservationProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, CourseReservationEvent> reservationConsumerFactory() {
        JsonDeserializer<CourseReservationEvent> deserializer = new JsonDeserializer<>(CourseReservationEvent.class);
        deserializer.addTrustedPackages("com.app.coursecenter.dto");
        return new DefaultKafkaConsumerFactory<>(commonConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourseReservationEvent> reservationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CourseReservationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(reservationConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(
                org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE
        );
        return factory;
    }

    // ===========================================================
    // ===============  COURSE RATING CONFIG  =====================
    // ===========================================================

    @Bean
    public ProducerFactory<String, CourseRatedEvent> ratingProducerFactory() {
        return new DefaultKafkaProducerFactory<>(commonProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, CourseRatedEvent> ratingKafkaTemplate() {
        return new KafkaTemplate<>(ratingProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, CourseRatedEvent> ratingConsumerFactory() {
        JsonDeserializer<CourseRatedEvent> deserializer = new JsonDeserializer<>(CourseRatedEvent.class);
        deserializer.addTrustedPackages("com.app.coursecenter.dto");
        return new DefaultKafkaConsumerFactory<>(commonConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourseRatedEvent> ratingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CourseRatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ratingConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(
                org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE
        );
        return factory;
    }
}