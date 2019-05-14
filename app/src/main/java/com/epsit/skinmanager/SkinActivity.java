package com.epsit.skinmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;

import java.io.File;

/**
 * 这个activity相当于baseActivity
 */
public class SkinActivity extends Activity {
    String TAG="SkinActivity";
    SkinInflateFactory skinInflateFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        skinInflateFactory = new SkinInflateFactory();
        //设置view的创建监听
        LayoutInflaterCompat.setFactory(getLayoutInflater(), skinInflateFactory);
        SkinManager.getInstance().init(getApplicationContext());
        //这个就是第三方的插件的资源文件

        //init();

    }
    public void init(){
        //String path = new File(Environment.getExternalStorageDirectory(),"skin.apk").getAbsolutePath();
        File file = new File( "/mnt/sdcard/skin.apk");
        String path = file.getAbsolutePath();

        if(file.exists()){
            SkinManager.getInstance().loadSkin(path);
        }else{
            Log.e(TAG,"皮肤不存在");
        }
    }
    public void update(){
        //对要换肤的view进行操作
        skinInflateFactory.update();
    }
}
