package com.zzh.rpc;

import java.lang.reflect.Proxy;
import java.net.Socket;

public class ClientProxy {
    private Socket socket;

    public ClientProxy(Socket socket) {
        this.socket = socket;
    }

    public <T> T createProxy(Class<?> interfaces){
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class<?>[] {interfaces},new ClientInvocationHandler(socket));
    }

}
