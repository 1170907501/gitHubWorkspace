package com.zzh.rpc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws IOException {
        /*ExecutorService executorService = Executors.newCachedThreadPool();

        *//**
         * 思路：rpc框架demo
         * 1、服务端接收到请求后，反射调用服务，然后通过 socket把结果发送给客户端
         *//*
        final int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        //定义一个线程池，把接收到的请求交给线程处理
        while(true){
            Socket socket = serverSocket.accept();
            executorService.execute(new ProcessorHandler(socket));
        }*/
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ((AnnotationConfigApplicationContext)context).start();
    }
}
