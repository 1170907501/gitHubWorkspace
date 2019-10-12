package com.zzh.rpc.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoveryWithZK implements IServiceDiscovery{

    private final String zkConfig = "127.0.0.1:2181";
    /**
     *  与服务端类似，需要通过curator在zk上查找服务地址
     *  2.很重要的一点，客户端注册服务端zk事件，如果服务端节点放生变化
     */
    //定义一个本地缓存Map
    List<String> serviceCache = new ArrayList<>();

    CuratorFramework curatorClient = null;
    {
        curatorClient = CuratorFrameworkFactory.builder().connectString(zkConfig).
                retryPolicy(new ExponentialBackoffRetry(5000,3))
                .sessionTimeoutMs(5000).namespace("/registry").build();
        curatorClient.start();
    }

    @Override
    public String serviceDiscovery(String serviceName) {

        String servicePath = "/"+serviceName;
        //首先判断本地缓存是否为空
        if(serviceCache.isEmpty()){
            //从zk上查找服务并放入
            try {
                serviceCache = curatorClient.getChildren().forPath(servicePath);
                //为啥在此处注册监听事件，因为只有第一次获取服务地址时需要注册一次
                bindWatch(servicePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //通过负载均衡方法获取某一个地址
        AbstractLoadBalanceStrategy loadBalanceStrategy = new RandomLoadBalanceStrategy();
        String serviceAddr = loadBalanceStrategy.getAddr(serviceCache);
        //在namespace下查找子节点
        return serviceAddr;
    }

    private void bindWatch(final String path) throws Exception {

        // 绑定子节点事件
        PathChildrenCache childrenCache = new PathChildrenCache(curatorClient,path,true);
        PathChildrenCacheListener listener = (CuratorFramework client, PathChildrenCacheEvent event)->{
            System.out.println("客户端收到节点变更的事件");
            serviceCache=curatorClient.getChildren().forPath(path);// 再次更新本地的缓存地址
        };
        childrenCache.getListenable().addListener(listener);
        childrenCache.start();
    }
}
