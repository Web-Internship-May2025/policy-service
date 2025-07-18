package com.example.policy_service.config;

import com.example.policy_service.message.PolicySearchCarResponseMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${spring.kafka.topic}")
    public String topic;

    @Bean NewTopic carRequest() {
        return TopicBuilder.name("policy.car.request")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean NewTopic carResponse() {
        return TopicBuilder.name("policy.car.response")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean NewTopic carDetailsRequest(){
        return TopicBuilder.name(topic).build();
    }

    @Bean
    public ConsumerFactory<String, PolicySearchCarResponseMessage> carResponseConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "policy-service-group");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.policy_service.message");

        JsonDeserializer<PolicySearchCarResponseMessage> deserializer =
                new JsonDeserializer<>(PolicySearchCarResponseMessage.class, false); // false disables type headers usage
        deserializer.addTrustedPackages("com.example.policy_service.message");
        deserializer.setUseTypeMapperForKey(false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PolicySearchCarResponseMessage> carResponseListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PolicySearchCarResponseMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(carResponseConsumerFactory());
        return factory;
    }
}
