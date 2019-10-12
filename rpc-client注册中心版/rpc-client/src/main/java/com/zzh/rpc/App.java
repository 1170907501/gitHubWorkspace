package com.zzh.rpc;

import java.io.IOException;
import java.net.Socket;

public class App {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",8085);
            //首先建立一个socket链接
            ClientProxy clientProxy = new ClientProxy(socket);
            IMcdsService mcdsService = clientProxy.createProxy(IMcdsService.class);
            System.out.println(mcdsService.saveMcds("zzh"));
            //首先建立一个socket链接
            Socket socket2 = new Socket("127.0.0.1",8085);
            ClientProxy clientProxy1 = new ClientProxy(socket2);
            IOrderService orderService = clientProxy1.createProxy(IOrderService.class);
            System.out.println(orderService.queryOrder("201939242342"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
