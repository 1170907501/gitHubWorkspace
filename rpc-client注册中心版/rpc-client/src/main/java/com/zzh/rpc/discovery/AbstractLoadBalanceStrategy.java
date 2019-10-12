package com.zzh.rpc.discovery;

import java.util.List;

public interface AbstractLoadBalanceStrategy {

    String getAddr(List<String> serviceAddrs);

}
