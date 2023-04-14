package com.qdbh.producer;

import com.qdbh.dataobject.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: ProducerDemoShunXu
 * 发送多条消息时 业务场景可能需要我们保证消息的顺序
 * 我们可以将消息发送到同一个队列中 这样消息消费的顺序性就可以得到保证
 * @author: wangshuo
 * @date: 2023-04-12 20:56:24
 */
public class ProducerDemoShunXu {

    public static void main(String[] args) throws Exception {
        //1.创建发送消息的对象
        DefaultMQProducer groupOne = new DefaultMQProducer("groupOne");
        //2.指定发送方对应的nameServer
        groupOne.setNamesrvAddr("43.138.10.236:9876");
        //3.启动Producer
        groupOne.start();
        //4.构建消息体
        Order sG = new Order(1, 1, "水果订单", 0);
        Order bL = new Order(11, 2, "菠萝订单", 1);
        Order cM = new Order(12, 2, "草莓订单", 1);
        Order cZ = new Order(13, 2, "橙子订单", 1);
        Order nZ = new Order(2, 1, "女装订单", 0);
        Order qZ = new Order(21, 2, "裙子订单", 2);
        Order mZ = new Order(22, 2, "帽子订单", 2);
        ArrayList<Order> orders = new ArrayList<>();
        Collections.addAll(orders, sG, bL, cM, cZ, nZ, qZ, mZ);
        orders.forEach((order) -> {
            Message message = new Message("OrderTopic", order.toString().getBytes());
            //5.发送消息
            try {
                SendResult sendResult = groupOne.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) { //让相同业务消息发送到同一个队列中
                        if (order.getParentOrderFlag() == 1)
                            return mqs.get(0); //直接写死 或者自定义业务算法 要注意数组下标越界异常
                        else
                            return mqs.get(1);
                    }
                }, null);
                System.out.println("队列编号:" + sendResult.getMessageQueue().getQueueId() + "   消息内容" + order.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        //6.关闭连接
        groupOne.shutdown();
    }
}
