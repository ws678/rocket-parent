package com.qdbh.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @description: ConsumerDemoShiWu2
 * 实现最大努力通知
 * @author: wangshuo
 * @date: 2023-04-15 11:08:59
 */
public class ConsumerDemoShiWu2 {

    public static void main(String[] args) throws Exception {
        //1.构建消费对象
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("groupOne");
        //2.指定NameServer
        defaultMQPushConsumer.setNamesrvAddr("43.138.10.236:9876");
        //3.绑定Topic
        defaultMQPushConsumer.subscribe("OrderTopic", "*");

        //过滤消息
        //defaultMQPushConsumer.subscribe("OrderTopic", "aa || bb || cc");

        //指定消费模式
        //defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING); //广播模式 多个DefaultMQPushConsumer可以同时接收到消息

        //4.开启一个监听消息
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {

                //接收到消息 执行业务代码
                // ※执行不成功的重试由MQ提供 可以配置重试次数和间隔时间 必须保证幂等性

                //回查事务状态可以定时器

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //返回成功状态
            }
        });
        //5.启动消费方服务
        defaultMQPushConsumer.start();
    }
}
