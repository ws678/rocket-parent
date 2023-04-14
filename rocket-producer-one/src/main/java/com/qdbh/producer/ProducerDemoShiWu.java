package com.qdbh.producer;

import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @description: 最终消息一致性 -- rocketMQ事务消息
 * tag过滤消息
 * @author: wangshuo
 * @date: 2023-04-13 16:11:28
 */
public class ProducerDemoShiWu {

    public static void main(String[] args) throws Exception {

        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("groupOne");
        transactionMQProducer.setNamesrvAddr("43.138.10.236:9876");
        transactionMQProducer.start();
        //设置事务监听
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                //在此处执行本地分布式事务 若本地事务执行失败 返回LocalTransactionState.ROLLBACK_MESSAGE 这样MQ就会将消息丢弃
                //业务代码 （5.1官网提供的业务demo太复杂冗余 小型系统没有千万级并发支撑需求 直接调用即可）
                /**
                 * 执行本地事务，并确定本地事务结果。
                 * 1. 如果本地事务提交成功，则提交消息事务。
                 * 2. 如果本地事务提交失败，则回滚消息事务。
                 * 3. 如果本地事务未知异常，则不处理，等待事务消息回查。
                 *
                 */
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) { //当事务消息状态为未知时 调用此方法
                System.out.println("事务补偿");
                //在此处进行本地分布式事务状态回查 （可将本地事务的结果持久化到数据库 UNKNOW后在此处进行回查）
                //业务代码
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        //设置消息体
        Message orderTopic = new Message("OrderTopic", "aa", "hello mq shiWu".getBytes()); //设置tag
        TransactionSendResult transactionSendResult = transactionMQProducer.sendMessageInTransaction(orderTopic, null);
        System.out.println("transactionSendResult = " + transactionSendResult);
        //transactionMQProducer.shutdown();
    }
}
