package com.zzh.rpc;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientInvocationHandler implements InvocationHandler {

    private Socket socket;

    public ClientInvocationHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * invoke 作用主要是把调用服务封装成 RpcRequest对象，然后通过socket发送给服务端
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion("v1.0");  //暂时把版本写死
        if(className.equals("com.zzh.rpc.IOrderService")){
            request.setVersion("");  //暂时把版本写死
        }
        //网络发送可以单独抽出方法
        Object result = send(request);
        return result;
    }

    private Object send(RpcRequest request){
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        Object result = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());
            result  = inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
