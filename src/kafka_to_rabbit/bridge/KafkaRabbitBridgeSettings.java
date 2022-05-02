package kafka_to_rabbit.bridge;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.effyis.template.annotation.Reloadable;

@Component
@Reloadable
@ManagedResource(objectName = "Effyis Kafka-Rabbit Bridge:name=Settings")
public class KafkaRabbitBridgeSettings {

	@Value("${kafka.consumer.bootstrap.servers:}")
	String   kafka_consumer_bootstrap_servers;   
	@Value("${kafka.topic.id:}")
	String   kafka_topic_id;   
	@Value("${kafka.group.id:}")
	String   kafka_group_id;   
	@Value("${kafka.zookeeper.session.timeout.ms:400}")
	String   kafka_zookeeper_session_timeout_ms;   
	@Value("${kafka.zookeeper.sync.time.ms:200}")
	String   kafka_zookeeper_sync_time_ms;   
	@Value("${kafka.auto.commit.interval.ms:}")
	String   kafka_auto_commit_interval_ms;

	@Value("#{new String('${kafka.header.forward:false}').equalsIgnoreCase('true')}")
	boolean header_forward;
	@Value("${kafka.header.rabbit.key.name:}")
	String   header_name;

	@Value("${kafka.lagreader.broker.list:}")
	String   kafka_lagreader_broker_list;   


	@PostConstruct
	public void init() {
	}
	public void printConfiguration() {

		System.out.println("");
		System.out.println("******************************************************************************");
		System.out.println("******************** Kafka consumer configuration ****************************");
		System.out.println("******************************************************************************");
		System.out.println("kafka.consumer.bootstrap.servers=                   " +  kafka_consumer_bootstrap_servers 			      );
		System.out.println("kafka.topic.id=                     " +  kafka_topic_id                           );
		System.out.println("kafka.group.id=                     " +  kafka_group_id                           );
		System.out.println("kafka.zookeeper.session.timeout.ms= " +  kafka_zookeeper_session_timeout_ms       );
		System.out.println("kafka.zookeeper.sync.time.ms=       " +  kafka_zookeeper_sync_time_ms             );
		System.out.println("kafka.auto.commit.interval.ms=      " +  kafka_auto_commit_interval_ms            );
		System.out.println("kafka.header.forward=               " +  header_forward            );
		System.out.println("kafka.header.rabbit.key.name=       " +  header_name            );
	}


	@ManagedAttribute
	public String getKafka_consumer_bootstrap_servers() {
		return kafka_consumer_bootstrap_servers;
	}
	
	@ManagedAttribute
	public void setKafka_consumer_bootstrap_servers(String kafka_consumer_bootstrap_servers) {
		this.kafka_consumer_bootstrap_servers = kafka_consumer_bootstrap_servers;
	}
	@ManagedAttribute
	public String getKafka_topic_id() {
		return kafka_topic_id;
	}
	@ManagedAttribute
	public void setKafka_topic_id(String kafka_topic_id) {
		this.kafka_topic_id = kafka_topic_id;
	}
	@ManagedAttribute
	public String getKafka_group_id() {
		return kafka_group_id;
	}
	@ManagedAttribute
	public void setKafka_group_id(String kafka_group_id) {
		this.kafka_group_id = kafka_group_id;
	}
	@ManagedAttribute
	public String getKafka_zookeeper_session_timeout_ms() {
		return kafka_zookeeper_session_timeout_ms;
	}
	@ManagedAttribute
	public void setKafka_zookeeper_session_timeout_ms(
			String kafka_zookeeper_session_timeout_ms) {
		this.kafka_zookeeper_session_timeout_ms = kafka_zookeeper_session_timeout_ms;
	}
	@ManagedAttribute
	public String getKafka_zookeeper_sync_time_ms() {
		return kafka_zookeeper_sync_time_ms;
	}
	@ManagedAttribute
	public void setKafka_zookeeper_sync_time_ms(String kafka_zookeeper_sync_time_ms) {
		this.kafka_zookeeper_sync_time_ms = kafka_zookeeper_sync_time_ms;
	}
	@ManagedAttribute
	public String getKafka_auto_commit_interval_ms() {
		return kafka_auto_commit_interval_ms;
	}
	@ManagedAttribute
	public void setKafka_auto_commit_interval_ms(
			String kafka_auto_commit_interval_ms) {
		this.kafka_auto_commit_interval_ms = kafka_auto_commit_interval_ms;
	}

	@ManagedAttribute
	public boolean isHeader_forward() {
		return header_forward;
	}
	@ManagedAttribute
	public void setHeader_forward(boolean header_forward) {
		this.header_forward = header_forward;
	}
	@ManagedAttribute
	public String getHeader_name() {
		return header_name;
	}
	@ManagedAttribute
	public void setHeader_name(String header_name) {
		this.header_name = header_name;
	}
	@ManagedAttribute
	public String getKafka_lagreader_broker_list() {
		return kafka_lagreader_broker_list;
	}
	@ManagedAttribute
	public void setKafka_lagreader_broker_list(String kafka_lagreader_broker_list) {
		this.kafka_lagreader_broker_list = kafka_lagreader_broker_list;
	}

}
