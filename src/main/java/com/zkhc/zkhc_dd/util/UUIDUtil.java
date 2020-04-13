package com.zkhc.zkhc_dd.util;

import java.util.UUID;

/**
 * @description 生成UUID
 */
public class UUIDUtil {
    /**
     * 获取一个新的UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
