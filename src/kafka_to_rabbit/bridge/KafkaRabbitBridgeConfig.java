package kafka_to_rabbit.bridge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaRabbitBridgeConfig {
	   
	   @Bean
	   public KafkaRabbitBridgeSettings  kafkaRabbitBridgeSettings() {
		   return new KafkaRabbitBridgeSettings (); 
	   }	

	   @Bean
	   public KafkaTopicLag  kafkaTopicLag() {
		   return new KafkaTopicLag (); 
	   }	
	   
}
