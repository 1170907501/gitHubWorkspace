package com.zzh.rpc;

import org.jboss.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;

public class ProcessorHandler implements Runnable{

    private Socket socket;
    private Map<String,Object> handleMap;

    public ProcessorHandler(Socket socket, Map<String,Object> handleMap) {
        this.socket = socket;
        this.handleMap = handleMap;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            //读取客户端发来的对象
            RpcRequest request = (RpcRequest)inputStream.readObject();
            String serviceInfo = request.getClassName();
            if(!StringUtils.isEmpty(request.getVersion())){
                serviceInfo+="-"+request.getVersion();
            }
            Object service = handleMap.get(serviceInfo);
            ServerProxy serverProxy = new ServerProxy(service);
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
