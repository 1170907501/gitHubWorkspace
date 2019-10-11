package com.gupaoedu.rpc;

import static org.junit.Assert.assertTrue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    public static void main(String[] args) throws Exception {
        final String zkConfig = "127.0.0.1:2181";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(zkConfig).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();

        createData(curatorFramework);
        //deleteData(curatorFramework);
         //getData(curatorFramework);
       // updateData(curatorFramework);

    }
    private static void createData(CuratorFramework curatorFramework) throws Exception { //EPHEMERAL
        List<ACL> aclList = new ArrayList<>();
        ACL readAcl = new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE);
        aclList.add(readAcl);
        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(aclList)
                .forPath("/authzzh");
    }

    private static void deleteData(CuratorFramework curatorFramework) throws Exception {

        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/data");
        Stat stat = new Stat();
        String value = new String(curatorFramework.getData().storingStatIn(stat).
                forPath("/data/program"));
        System.out.println(value);
        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/data/program");


    }

    private static void getData(CuratorFramework curatorFramework) throws Exception {
        System.out.println(new String(curatorFramework.getData().forPath("/data")));
    }

    private static void updateData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/data","up".getBytes());
    }
}
