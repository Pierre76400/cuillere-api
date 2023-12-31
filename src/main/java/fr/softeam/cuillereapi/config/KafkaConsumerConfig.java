package fr.softeam.cuillereapi.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {


	@Bean
	public ConsumerFactory<String, String> consumerFactory(KafkaProperties properties) {
		Map<String, Object> props  = properties.buildProducerProperties();

		props.put(
			ConsumerConfig.GROUP_ID_CONFIG,
			"cuillere-api");

		props.put(
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
			StringDeserializer.class);
		props.put(
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
			JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "fr.softeam.cuillereapi.api");
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> 	kafkaListenerContainerFactory(KafkaProperties properties) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory(properties));
		return factory;
	}
}