package fr.softeam.cuillereapi.service;
import fr.softeam.cuillereapi.api.AvisCreationDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvisService {

	private static final String TOPIC_CREATION_AVIS = "cuillere-avis";
	private final KafkaTemplate<String, AvisCreationDto> kafkaAvisTemplate;

	public KafkaAvisService(KafkaTemplate<String, AvisCreationDto> kafkaAvisTemplate) {
		this.kafkaAvisTemplate = kafkaAvisTemplate;
	}

	public void sendMessage(AvisCreationDto avisCreationDto) {
		kafkaAvisTemplate.send(TOPIC_CREATION_AVIS, avisCreationDto);
	}
}
