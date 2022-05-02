package kafka_to_rabbit.bridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "Effyis Kafka-Rabbit Bridge:name=TopicLag")
public class KafkaTopicLag 
{
	@Autowired
	KafkaRabbitBridgeSettings settings;


	boolean isOpen = false;	
	KafkaConsumer kafkaConsumer;


	/*
	 * Input parameters example:
	 * kafkatopic: sina.salesforce.main.out
	 * kafkagroup: dummy1
	 * brokerList: 192.168.7.124,192.168.7.125,192.168.7.126
	 */

	public  void init() 
	{
		try
		{
			Properties props = new Properties();
			props.put("bootstrap.servers", settings.kafka_consumer_bootstrap_servers.replaceAll("2181", "9092"));
			props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
			props.put("group.id", settings.kafka_group_id);
			kafkaConsumer = new KafkaConsumer(props);	
			List<TopicPartition> partitions = new ArrayList<TopicPartition>();

			List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(settings.kafka_topic_id); 
			if (partitionInfos != null)
			{
				for (PartitionInfo partition : partitionInfos)
					partitions.add(new TopicPartition(partition.topic(),
							partition.partition()));
				kafkaConsumer.assign(partitions); 
			}
			isOpen = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void close() 
	{
		if (!isOpen) return;
		try
		{
			kafkaConsumer.close();
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}


	@ManagedAttribute
	public HashMap<Integer,Long> getLag() 
	{
		HashMap<Integer,Long> lagMap = new HashMap<Integer,Long>();
		try
		{	
			Set<TopicPartition> partitionSet = kafkaConsumer.assignment();
			Map<TopicPartition, Long> endoffsetMap = kafkaConsumer.endOffsets( partitionSet);
			for(TopicPartition tempTopic: partitionSet)
			{	
				long currOffset = kafkaConsumer.position(tempTopic);
				long endOffset = endoffsetMap.get(tempTopic);
				long lag = endOffset - currOffset;
				lagMap.put(tempTopic.partition(), lag);
			}		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lagMap;

	}

	@ManagedAttribute
	public long getMinLag() 
	{
		HashMap<Integer,Long> lagMap = getLag();
		return Collections.min(lagMap.values());		
	}

	@ManagedAttribute
	public long getMaxLag() 
	{
		HashMap<Integer,Long> lagMap = getLag();
		return Collections.max(lagMap.values());			
	}

	@ManagedAttribute
	public HashMap<Integer,Long> getLogSize() 
	{
		HashMap<Integer,Long> logSizeMap = new HashMap<Integer,Long>();
		try
		{
			Set<TopicPartition> partitionSet = kafkaConsumer.assignment();
			Map<TopicPartition, Long> endoffsetMap = kafkaConsumer.endOffsets( partitionSet);
			for(TopicPartition tempTopic: partitionSet)
			{	
				long endOffset = endoffsetMap.get(tempTopic);			
				logSizeMap.put(tempTopic.partition(), endOffset);
			}					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return logSizeMap;
	}

	@ManagedAttribute
	public long getMinLogSize() 
	{
		// Get log size
		HashMap<Integer,Long> logSizeMap = getLogSize() ; 
		return  Collections.min(logSizeMap.values());	
	}

	@ManagedAttribute
	public long getMaxLogSize() 
	{
		// Get log size
		HashMap<Integer,Long> logSizeMap = getLogSize() ; 
		return  Collections.max(logSizeMap.values());	
	}

	@ManagedAttribute
	public HashMap<Integer,Long> getOffset() 
	{
		HashMap<Integer,Long> offsetMap = new HashMap<Integer,Long>();
		try
		{
			Set<TopicPartition> partitionSet = kafkaConsumer.assignment();
			for(TopicPartition tempTopic: partitionSet)
			{	
				long currOffset = kafkaConsumer.position(tempTopic);
				offsetMap.put(tempTopic.partition(), currOffset);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return offsetMap;
	}

	@ManagedAttribute
	public long getMinOffset() 
	{
		HashMap<Integer,Long> offsetMap = getOffset();
		return  Collections.min(offsetMap.values());	
	}

	@ManagedAttribute
	public long getMaxOffset() 
	{
		HashMap<Integer,Long> offsetMap = getOffset();
		return  Collections.max(offsetMap.values());		
	}

}
