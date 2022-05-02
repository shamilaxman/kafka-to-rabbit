package kafka_to_rabbit.bridge;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.effyis.template.TemplatePattern;
import com.effyis.template.pattern.producer.MessageProviderInterface;
import com.effyis.template.pattern.producer.ProviderCreatorInterface;


public class KafkaRabbitBridge implements ProviderCreatorInterface {

	static KafkaReader kafkaReader;

	static KafkaRabbitBridgeSettings kafkaRabbitBridgeSettings;

	static KafkaTopicLag kafkaTopicLag;

	public static void main(String[] args) throws Exception {

		KafkaRabbitBridgeTemplate ef = new KafkaRabbitBridgeTemplate(TemplatePattern.PRODUCER_PATTERN);
		ef.init("kafka2rabbit.bridge.properties"); //init from config file

		KafkaRabbitBridge example = new KafkaRabbitBridge();
		ef.setProviderCreator(example); //setup message handler
		kafkaRabbitBridgeSettings = ef.getContext().getBean(KafkaRabbitBridgeSettings.class);
		kafkaRabbitBridgeSettings.printConfiguration();
		kafkaTopicLag = ef.getContext().getBean(KafkaTopicLag.class);

		kafkaReader = new KafkaReader();

		ef.run(); //Runs listener container
		ef.shutdown();
	}

	@Override
	public MessageProviderInterface create(int id) throws Exception {
		if (!kafkaReader.isOpen) kafkaReader.init(); 
		return kafkaReader;
	}

	public static class KafkaReader implements MessageProviderInterface {

		boolean isOpen;
		private  KafkaConsumer<String, String>  consumer;

		public KafkaReader() throws Exception {
			super();
		}


		public void init() 
		{

			
			Properties props = new Properties();
			props.put("bootstrap.servers", kafkaRabbitBridgeSettings.kafka_consumer_bootstrap_servers.replaceAll("2181", "9092"));
			props.put("group.id", kafkaRabbitBridgeSettings.kafka_group_id);
			props.put("enable.auto.commit",true);
			props.put("auto.commit.interval.ms", kafkaRabbitBridgeSettings.kafka_auto_commit_interval_ms);

			props.put("max.poll.records", 1);
			props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
			props.put("key.deserializer.encoding", "UTF-8");
			props.put("value.deserializer.encoding", "UTF-8");
//			props.put("auto.offset.reset", "earliest");

			consumer = new KafkaConsumer<String, String>(props);

			Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>(); 
			List<PartitionInfo> partitionInfos = null;		
			List<TopicPartition> partitions = new ArrayList<TopicPartition>();


			List<String> kafkatopicArr = Arrays.asList(kafkaRabbitBridgeSettings.kafka_topic_id);

			for(String temptopic : kafkatopicArr)
			{
				partitionInfos = consumer.partitionsFor(temptopic); 
				if (partitionInfos != null)
				{
					for (PartitionInfo partition : partitionInfos)
						partitions.add(new TopicPartition(partition.topic(),
								partition.partition()));
					consumer.assign(partitions); 
				}
			}
			kafkaTopicLag.init();

			isOpen = true;
		}


		@Override
		public synchronized Message provideMessage() throws Exception 
		{
			ConsumerRecords<String, String> records = consumer.poll(1000);

			for (ConsumerRecord<String, String> record : records)
			{				
				MessageProperties mp = new MessageProperties();
				if (kafkaRabbitBridgeSettings.header_forward) 
				{
					mp.setHeader(kafkaRabbitBridgeSettings.header_name, record.key());
				}
				return new Message(record.value().getBytes(), mp);
			}			
			return null;
		}

		@Override
		public void close() {
			kafkaTopicLag.close();
			consumer.close();
			isOpen = false;
		}

	}


}
