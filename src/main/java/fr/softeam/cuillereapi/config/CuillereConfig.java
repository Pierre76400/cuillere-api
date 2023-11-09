package fr.softeam.cuillereapi.config;

import fr.softeam.cuillereapi.api.AvisCreationDto;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class CuillereConfig {


	@Bean
	public KafkaTemplate<String, AvisCreationDto> avisKafkaTemplate(KafkaProperties properties)
	{
		Map<String, Object> props = properties.buildProducerProperties();

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		KafkaTemplate<String, AvisCreationDto> template = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
		return template;
	}
/*
	@Bean
	public KafkaTemplate<String, AvisCreationDto> avisKafkaTemplate() {
		return new KafkaTemplate<>(producerFactory() );
	}


	@Bean
	public ProducerFactory<String, AvisCreationDto> producerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<String, AvisCreationDto>(props);
	}
*/
@Bean
public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
	return new ObservedAspect(observationRegistry);
}
}
