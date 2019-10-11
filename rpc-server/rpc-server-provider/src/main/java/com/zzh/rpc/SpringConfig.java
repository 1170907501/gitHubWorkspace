package com.zzh.rpc;

import com.zzh.rpc.annotation.RpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean(name = "rpcService")
    public RpcServiceScan RpcService(){
        return new RpcServiceScan();
    }
}
