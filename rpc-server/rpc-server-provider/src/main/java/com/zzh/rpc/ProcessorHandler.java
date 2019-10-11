package com.zzh.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ProcessorHandler implements Runnable{

    private Socket socket;

    public ProcessorHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            //读取客户端发来的对象
            RpcRequest request = (RpcRequest)inputStream.readObject();
            IMcdsService mcdsService = new McdsService();
            ServerProxy serverProxy = new ServerProxy(mcdsService);
            Object result = serverProxy.invoke(request);
            ServerNetTransport transport = new ServerNetTransport(socket);
            transport.send(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
