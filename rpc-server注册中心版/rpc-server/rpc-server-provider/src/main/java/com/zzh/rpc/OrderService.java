package com.zzh.rpc;

import com.zzh.rpc.annotation.RpcService;

@RpcService(value=IOrderService.class)
public class OrderService implements IOrderService{

    @Override
    public String queryOrder(String orderId) {
        System.out.println("queryOrder:"+orderId);
        return "查询订单详情："+orderId;
    }
}
