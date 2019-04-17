package com.shop.until;

import java.util.UUID;

public class CommonUtils {
    public  static String getId(){
        return UUID.randomUUID().toString();
    }
}
