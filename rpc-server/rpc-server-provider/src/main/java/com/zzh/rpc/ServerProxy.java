package com.zzh.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ServerProxy {
    private IMcdsService mcdsService;

    public ServerProxy(IMcdsService mcdsService) {
        this.mcdsService = mcdsService;
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
            result = method.invoke(mcdsService,args);
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
