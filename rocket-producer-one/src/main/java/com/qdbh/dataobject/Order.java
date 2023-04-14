package com.qdbh.dataobject;

/**
 * @description: Order
 * @author: wangshuo
 * @date: 2023-04-12 20:54:17
 */
public class Order {

    private int id;
    private int parentOrderFlag; //1： 父级订单 、 2：子订单
    private String msg;
    private int parentId;

    public Order(int id, int parentOrderFlag, String msg, int parentId) {
        this.id = id;
        this.parentOrderFlag = parentOrderFlag;
        this.msg = msg;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentOrderFlag() {
        return parentOrderFlag;
    }

    public void setParentOrderFlag(int parentOrderFlag) {
        this.parentOrderFlag = parentOrderFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", parentOrderFlag=" + parentOrderFlag +
                ", msg='" + msg + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
