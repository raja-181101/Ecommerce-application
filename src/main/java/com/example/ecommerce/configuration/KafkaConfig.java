package com.example.ecommerce.configuration;


import com.example.ecommerce.dataToobject.OrderDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String,Object> producerFactory(){

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, OrderDto> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        config.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> orderKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092",
                        ConsumerConfig.GROUP_ID_CONFIG,"my-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JacksonJsonDeserializer.class,
                        JacksonJsonDeserializer.TRUSTED_PACKAGES,"*"
                ),new StringDeserializer(),new JacksonJsonDeserializer<>(OrderDto.class))
        );
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate()));

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> responseKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092",
                        ConsumerConfig.GROUP_ID_CONFIG,"order-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JacksonJsonDeserializer.class,
                        JacksonJsonDeserializer.TRUSTED_PACKAGES,"*"
                ),new StringDeserializer(),new JacksonJsonDeserializer<>(OrderDto.class))
        );
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate()));

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate){
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,((consumerRecord, e) -> new TopicPartition("order-dlt",0)));
        FixedBackOff backOff = new FixedBackOff(2000L,3);
        return  new DefaultErrorHandler(recoverer,backOff);
    }

}
