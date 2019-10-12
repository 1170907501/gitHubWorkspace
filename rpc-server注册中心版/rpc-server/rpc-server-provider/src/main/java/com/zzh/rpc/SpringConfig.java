package com.zzh.rpc;

import com.zzh.rpc.annotation.RpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.zzh.rpc")
public class SpringConfig {

    @Bean(name = "rpcService")
    public RpcServiceScan RpcServiceScan(){
        return new RpcServiceScan(8085);
    }
}
