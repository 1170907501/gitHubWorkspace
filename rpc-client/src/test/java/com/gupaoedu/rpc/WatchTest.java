package com.gupaoedu.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class WatchTest {

    public static void main(String[] args) throws Exception {
        final String zkConfig = "127.0.0.1:2181";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(zkConfig).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
        addListenerWithNode(curatorFramework);
    }
    private static void addListenerWithNode(CuratorFramework curatorFramework) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener listener = ()->{
            System.out.println("receive node Changed");
            System.out.println(nodeCache.getCurrentData().getPath()+"==="+new String(nodeCache.getCurrentData().getData()));
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
    }

    private static void addListenerWithChild(CuratorFramework curatorFramework)throws Exception{
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener childrenCacheListener = (curatorFramework1,pathChildrenCacheEvent)->{
            System.out.println(pathChildrenCacheEvent.getType()+"->"+pathChildrenCacheEvent.getData().getData().toString());
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        childrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }

}
