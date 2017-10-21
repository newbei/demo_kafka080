package com.newbei.kafka.kafka08;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 消息消费端
 * 
 * @author 迦壹
 * @Time 2014-08-05
 */
public class Consumertest extends Thread {

	private final ConsumerConnector consumer;
	private final String topic;

	public static void main(String[] args) {
		Consumertest consumerThread = new Consumertest("newbei");
		consumerThread.start();
	}

	public Consumertest(String topic) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		// 设置zookeeper的链接地址
		props.put("zookeeper.connect", "learn1:2181,learn2:2181,learn3:2181");
		// 设置group id
		props.put("group.id", "test_g_02");
		// kafka的group 消费记录是保存在zookeeper上的, 但这个信息在zookeeper上不是实时更新的, 需要有个间隔时间更新
		props.put("auto.commit.interval.ms", "1000");
		props.put("zookeeper.session.timeout.ms", "10000");
		props.put("auto.offset.reset", "smallest");
		return new ConsumerConfig(props);
	}

	public void run() {
		// 设置Topic=>Thread Num映射关系, 构建具体的流
		Map<String, Integer> topickMap = new HashMap<String, Integer>();
		topickMap.put(topic, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topickMap);
		KafkaStream<byte[], byte[]> stream = streamMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		System.out.println("*********Results********");
		while (it.hasNext()) {
			System.err.println("get data:" + new String(it.next().message()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
