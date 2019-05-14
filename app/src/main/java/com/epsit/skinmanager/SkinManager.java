package com.epsit.skinmanager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * AssetManager也是用来加载第三方包里的文件资源的，主要用到的是反射
 */
public class SkinManager {
    String TAG ="SkinManager";
    /**
     * 代表插件apk里，资源文件
     */
    private Resources skinResources;
    private Context context;
    private static SkinManager instance = new SkinManager();

    //插件的包名
    private String skinPackage;
    public void init(Context context){
        this.context = context;
    }
    public static SkinManager getInstance() {
        return instance;
    }

    private SkinManager() {

    }

    public void loadSkin(String path){
        //它可以打开第三方的app
        PackageManager packageManager = context.getPackageManager();

        //获取外置apk的信息，第二个参数表示获取什么信息，这里表示获取所有的activity的信息
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        skinPackage = packageInfo.packageName;
        try {
            //创建了一个空的assetManager对象
            AssetManager assetManager = AssetManager.class.newInstance();
            //最主要的是给这个assetManager设置资源

            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, path);

            Resources resources = context.getResources();

            skinResources = new Resources(assetManager,resources.getDisplayMetrics(),resources.getConfiguration());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取第三方插件文件里头的资源颜色  比如有 R.color.colorPrimary  （这个第三方插件里有，当前的资源里也有）
     * @param resId
     * @return 返回的颜色值具体是当前的资源里的颜色还是第三方插件里的颜色呢（名称都一样的），
     * 看第三方插件是否下载
     */
    public int getColor(int resId){
        if(skinResources == null){ //没下载第三方皮肤资源
            return resId;
        }
        //我们传来的参数是R.color.colorPrimary,下面方法可以得到 colorPrimary 这么一个字符串，表示资源名字
        String resName = context.getResources().getResourceEntryName(resId);
        //第二个参数是类型：R.drawable.colorPrimary  也可能有R.drawable.colorPrimary，所以第二个参数表示什么类型
        int trueResid = skinResources.getIdentifier(resName, "color" , skinPackage);
        //上面这个返回得到的trueResid相当于当前app里的R.color.colorPrimary一样，表示第三方插件里唯一的资源的id

        int color = skinResources.getColor(trueResid);
        Log.e(TAG,"color-->"+color+"  resName="+resName+"  trueResid="+trueResid);
        return color;
    }
    public Drawable getDrawable(int resId){
        if(skinResources == null){ //没下载第三方皮肤资源
            return context.getResources().getDrawable(resId);
        }
        String resName = context.getResources().getResourceEntryName(resId);
        int trueResid = skinResources.getIdentifier(resName, "drawable" , skinPackage);
        Drawable drawable = skinResources.getDrawable(trueResid);
        return drawable;
    }
}
