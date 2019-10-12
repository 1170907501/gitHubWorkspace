package com.zzh.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ServerProxy {
    private Object service;

    public ServerProxy(Object service) {
        this.service = service;
    }

    public Object invoke(RpcRequest request){
        Object result = null;
        try {
            String className = request.getClassName();
            String methodName = request.getMethodName();
            Object[] args = request.getParameters();
            Class<?>[] paramTypes = new Class<?>[args.length];
            for(int i=0;i<args.length;i++){
                paramTypes[i] = args[i].getClass();
            }
            Class clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName,paramTypes);
            result = method.invoke(service,args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
