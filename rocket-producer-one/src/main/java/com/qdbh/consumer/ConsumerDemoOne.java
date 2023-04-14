package com.qdbh.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @description: ConsumerDemoOne
 * 一对一模式
 * 广播模式
 * 异步消息
 * 单向消息
 * 延时消息
 * 批量消息
 * 过滤消息
 * @author: wangshuo
 * @date: 2023-04-12 17:30:12
 */
public class ConsumerDemoOne {

    public static void main(String[] args) throws Exception {

        //1.构建消费对象
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("groupOne");
        //2.指定NameServer
        defaultMQPushConsumer.setNamesrvAddr("43.138.10.236:9876");
        //3.绑定Topic
        //defaultMQPushConsumer.subscribe("OrderTopic", "*");

        //过滤消息
        defaultMQPushConsumer.subscribe("OrderTopic", "aa || bb || cc");

        //指定消费模式
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING); //广播模式 多个DefaultMQPushConsumer可以同时接收到消息

        //4.开启一个监听消息
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                list.forEach((a) -> {
                    System.out.println("消费到消息：" + new String(a.getBody()));
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //返回成功状态
            }
        });
        //5.启动消费方服务
        defaultMQPushConsumer.start();
    }
}
