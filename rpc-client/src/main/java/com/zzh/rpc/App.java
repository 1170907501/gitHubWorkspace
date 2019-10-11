package com.zzh.rpc;

import java.io.IOException;
import java.net.Socket;

public class App {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8080);
            //首先建立一个socket链接
            ClientProxy clientProxy = new ClientProxy(socket);
            IMcdsService mcdsService = clientProxy.createProxy(IMcdsService.class);
            System.out.println(mcdsService.saveMcds("zzh"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
