package com.ty.voogla.bean;

import android.util.Log;

/**
 * @author TY on 2019/1/5.
 */
public class Test {

    private void function(String name,String age){
        Log.d(name,age);
    }

    private void function(String name,int age){
        Log.d(name,age + "");
    }
}
