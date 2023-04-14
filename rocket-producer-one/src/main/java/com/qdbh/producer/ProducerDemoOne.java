package com.qdbh.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @description: ProducerDemoOne
 * 一对一模式
 * 广播模式
 * 异步消息
 * 单向消息
 * 延时消息
 * 批量消息
 * @author: wangshuo
 * @date: 2023-04-12 17:24:08
 */
public class ProducerDemoOne {

    public static void main(String[] args) throws Exception {

        //1.创建发送消息的对象
        DefaultMQProducer groupOne = new DefaultMQProducer("groupOne");
        //2.指定发送方对应的nameServer
        groupOne.setNamesrvAddr("43.138.10.236:9876");
        //3.启动Producer
        groupOne.start();
        //4.构建消息体
        Message orderTopic = new Message("OrderTopic", "hello mq".getBytes());
        //五 、 发送消息

        //发送同步消息
        //SendResult send = groupOne.send(orderTopic);
        //System.out.println("sendResult = " + send);

        //发送异步消息 MQ接收到消息后会调用回调函数 此期间线程处于阻塞状态
        /*groupOne.send(orderTopic, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("sendResult = " + sendResult);
                groupOne.shutdown(); //将关闭连接移动到此处 避免提前关闭连接 接收不到发送结果
            }

            @Override
            public void onException(Throwable e) {

                System.out.println("发生异常:" + e);
            }
        });*/

        //发送单向消息
        //groupOne.sendOneway(orderTopic);

        //发送延时消息
        /*for (int i = 0; i < 10; i++) {
            String body = "hello mq " + i;
            Message yanShi = new Message("OrderTopic", body.getBytes());
            if (i == 3)
                yanShi.setDelayTimeLevel(3); //第四条消息延时10秒
            SendResult sendResult = groupOne.send(yanShi);
            System.out.println("sendResult = " + sendResult);
        }*/

        //发送批量消息 只需要将多条消息全部放入集合中即可
        Message message1 = new Message("OrderTopic", "hello mq1".getBytes());
        Message message2 = new Message("OrderTopic", "hello mq2".getBytes());
        Message message3 = new Message("OrderTopic", "hello mq3".getBytes());
        ArrayList<Message> messages = new ArrayList<>();
        Collections.addAll(messages, message1, message2, message3);
        //messages.forEach(System.out::println);
        SendResult sendResult = groupOne.send(messages);
        System.out.println("sendResult = " + sendResult);

        //6.关闭连接
        groupOne.shutdown();
    }
}
