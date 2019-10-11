package com.zzh.rpc;

import org.springframework.stereotype.Service;

@Service("mcdsService")
public class McdsService implements IMcdsService{
    @Override
    public String saveMcds(String mcdsInfo) {
        System.out.println("saveMcds:mcdsInfo"+mcdsInfo);
        return "保存商品成功";
    }
}
