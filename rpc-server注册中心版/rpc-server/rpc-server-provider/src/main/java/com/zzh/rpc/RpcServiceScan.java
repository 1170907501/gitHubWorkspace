package com.zzh.rpc;

import com.zzh.rpc.annotation.RpcService;
import com.zzh.rpc.registry.RegisterCenterWithZKImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个类主要是通过注解把所有bean放入本地缓存
 */
public class RpcServiceScan implements InitializingBean,ApplicationContextAware{
    private int port;

    public RpcServiceScan(int port) {
        this.port = port;
    }

    Map<String,Object> handleMap = new HashMap<>();
    ExecutorService executorService = Executors.newCachedThreadPool();
    private RegisterCenterWithZKImpl registerCenterWithZK = new RegisterCenterWithZKImpl();
    //afterPropertiesSet,在bean的属性设置完毕后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        //启动线程池，接收客户端链接
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = null;
        while(true){
            socket = serverSocket.accept();
            executorService.execute(new ProcessorHandler(socket,handleMap));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);

        // 从注解中获取service
        if(!beans.isEmpty()){
            for(Object bean:beans.values()){
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if(!StringUtils.isEmpty(version)){
                    serviceName+="-"+version;
                }
                handleMap.put(serviceName,bean);
                registerCenterWithZK.registry(serviceName,getAddress()+":"+port);
            }
        }
    }
    private static String getAddress(){
        InetAddress inetAddress=null;
        try {
            inetAddress= InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();// 获得本机的ip地址
    }
}
