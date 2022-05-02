package kafka_to_rabbit.bridge;

import com.effyis.template.TemplatePattern;
import com.effyis.template.pattern.producer.EffyisProducerTemplate;

public class KafkaRabbitBridgeTemplate extends EffyisProducerTemplate {

	public KafkaRabbitBridgeTemplate(TemplatePattern templatePattern) {
		super(templatePattern);
	}

	@Override
	public void registerPatternContext() throws Exception {
		super.registerPatternContext();
		ctx.register(KafkaRabbitBridgeConfig.class);
	}

	@Override
	public void printConfiguration() {
		// TODO Auto-generated method stub
		super.printConfiguration();
	}
	
	
}
