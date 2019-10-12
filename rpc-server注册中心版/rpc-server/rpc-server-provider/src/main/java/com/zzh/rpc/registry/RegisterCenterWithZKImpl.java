package com.zzh.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class RegisterCenterWithZKImpl implements IRegistryCenter {

    private final String zkConfig = "localhost:2181";
    CuratorFramework curator = null;
    {
        curator = CuratorFrameworkFactory.builder().connectString(zkConfig).
                retryPolicy(new ExponentialBackoffRetry(5000,3)).
                sessionTimeoutMs(5000).connectionTimeoutMs(5000)
                .namespace("registry").build();
        curator.start();
    }

    /**
     * 注册中心的本质是：使用curator客户端在服务器上创建节点【供客户端获取】
     * @param serverName
     * @param serverAddr
     */
    @Override
    public void registry(String serverName,String serverAddr) {
        System.out.println("注册中心开始注册"+serverName+"服务");
        try {
            String serverNode = "/"+serverName;
            //首先判断服务节点是否存在 checkExists
            if(curator.checkExists().forPath(serverNode)==null){
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                        .forPath(serverNode);
            }
            //注册中心子节点信息 serverAddr:   ip:port
            // 子节点添加时要加上父节点路径,因为注册中心要动态感知服务节点【通过心跳机制】，如果服务挂掉则临时节点会被删除
            String addrPath = serverNode+ "/" +serverAddr;
            curator.create().withMode(CreateMode.EPHEMERAL).forPath(addrPath);
            System.out.println(serverName+"服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
