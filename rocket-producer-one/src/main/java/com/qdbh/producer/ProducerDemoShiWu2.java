package com.qdbh.producer;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

/**
 * @description: ProducerDemoShiWu2
 * 最大努力通知 -- rocketMQ实现事务消息
 * @author: wangshuo
 * @date: 2023-04-14 21:48:05
 */
public class ProducerDemoShiWu2 {

    public static void main(String[] args) throws Exception {

        //执行业务代码 成功后向MQ发送通知消息
        //成功后将处理结果持久化到数据库 方便回查

        //1.创建发送消息的对象
        DefaultMQProducer groupOne = new DefaultMQProducer("groupOne");
        //2.指定发送方对应的nameServer
        groupOne.setNamesrvAddr("43.138.10.236:9876");
        //3.启动Producer
        groupOne.start();
        //4.构建消息体
        Message orderTopic = new Message("OrderTopic", "hello mq".getBytes());
        //五 、 发送消息

        //发送普通消息
        SendResult send = groupOne.send(orderTopic);

        //6.关闭连接
        //groupOne.shutdown();
    }

    //对外提供查询充值记录接口 其实是一种主动补偿机制 当MQ出现问题我们可以由业务人员或者用户主动手动调用补偿 在这里将业务修复 而不是直接线上操作数据库
    public void selectStatus(/* 参数 */) {
        //若发送方事务已经执行成功 这里也要执行业务代码
    }
}
