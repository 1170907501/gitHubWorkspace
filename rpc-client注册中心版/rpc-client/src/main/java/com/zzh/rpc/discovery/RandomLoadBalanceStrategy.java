package com.zzh.rpc.discovery;

import java.util.List;
import java.util.Random;

public class RandomLoadBalanceStrategy implements AbstractLoadBalanceStrategy{
    @Override
    public String getAddr(List<String> serviceAddrs) {

        int length = serviceAddrs.size();
        Random random = new Random();
        return serviceAddrs.get(random.nextInt(length));
    }
}
