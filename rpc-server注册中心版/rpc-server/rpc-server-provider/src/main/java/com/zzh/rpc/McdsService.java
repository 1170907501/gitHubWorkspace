package com.zzh.rpc;

import com.zzh.rpc.annotation.RpcService;

@RpcService(value=IMcdsService.class,version = "v1.0")
public class McdsService implements IMcdsService{
    @Override
    public String saveMcds(String mcdsInfo) {
        System.out.println("saveMcds:mcdsInfo"+mcdsInfo);
        return "保存商品成功";
    }
}
