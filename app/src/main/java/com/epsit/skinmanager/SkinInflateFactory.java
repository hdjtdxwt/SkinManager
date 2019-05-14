package com.epsit.skinmanager;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 监听整个界面 控件的创建过程
 */
public class SkinInflateFactory implements LayoutInflaterFactory {
    String TAG ="InflateFactory";

    //系统自带控件在xml里没有带包名的，我们自己补全
    private static final String[] prefixList = {
        "android.widget.","android.view.","android.webkit."
    };
    //需要换肤的view的map
    public HashMap<View,SkinItem> map = new HashMap<>();
    //layoutInflater调用
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.e(TAG,"-------------");
        /**
         * 做实例化view的操作
         *
         * 得到view之后决定是否换背景颜色
         */
        View view = null;

        //区分自定义控件和非自定义控件
        if(name.indexOf(".")==-1){ //系统的view
            if(!name.equals("ViewStub")){
                for(String prefix:prefixList){
                    view = createView(context, attrs,prefix+name);
                    if(view!=null){
                        Log.e(TAG,"创建出来view了："+view.getClass().getName());
                        break;//view创建出来了，可以不用循环了
                    }
                }
            }
        }else{ //自定义的view
            view = createView(context,attrs,name);
        }
        //在view创建了之后，进行换肤的判断
        if(view!=null){
            parseSkinView(view, context, attrs);
        }
        return view;
    }

    /**
     * 换肤，判断第三方的插件里是否有相同的属性
     * @param view
     * @param context
     * @param attrs
     */
    private void parseSkinView(View view, Context context, AttributeSet attrs) {
        List<SkinInterface> attrList = new ArrayList<>();

        //换肤，换的是背景或者字体的颜色
        for(int i=0;i<attrs.getAttributeCount();i++){
            //得到属性名称
            String attrName = attrs.getAttributeName(i);
            //得到属性值
            String attrValue = attrs.getAttributeValue(i);

            SkinInterface skinInterface = null;
            int id = -1;//资源的id
            //得到的值为  colorPrimary 或者  text_color_selector  就是android:color="@color/colorPrimary" 属性值中/后面的
            String entryName;
            //比如color  drawable
            String typeName;

            if("background".equals(attrName)){
                Log.e(TAG,"--背景图片："+attrValue);
                id = Integer.parseInt(attrValue.substring(1));
                entryName = context.getResources().getResourceEntryName(id);
                typeName = context.getResources().getResourceTypeName(id);
                Log.e(TAG,"attrName="+attrName+"  id="+id+"  entryName="+entryName+"  typeName="+typeName);
                skinInterface = new BackgroundSkin(attrName, id, entryName,typeName);
            }else if("textColor".equals(attrName)){
                /**
                 * @color/colorPrimary
                 * 在变成了R.class类里头后，全是 @212123 这样格式的，有一个@在前头
                 */
                Log.e(TAG,"--字体颜色："+attrValue);
                id = Integer.parseInt(attrValue.substring(1));
                entryName = context.getResources().getResourceEntryName(id);
                typeName = context.getResources().getResourceTypeName(id);
                skinInterface = new TextSkin(attrName, id, entryName,typeName);
            }
            if(skinInterface!=null){
                attrList.add(skinInterface);
            }
        }
        SkinItem skinItem = new SkinItem(view,attrList);
        map.put(view, skinItem);
        skinItem.apply();
    }

    /**
     * 给外面进行调用的，一般是默认的颜色已经设置好了，某一个时机触发修改的方法
     */
    public void update(){
        Log.e(TAG," update()");
        for(View view:map.keySet()){
            if(view!=null){
                SkinItem targetView = map.get(view);
                Log.e(TAG,"targetView="+targetView.getClass().getName());
                targetView.apply();
            }
        }
    }
    class SkinItem{
        private View view;
        private List<SkinInterface>skinInterfaces;
        public SkinItem(View view, List<SkinInterface>list){
            this.view = view;
            this.skinInterfaces = list;
        }
        public void apply(){
            for(SkinInterface skinInterface:skinInterfaces){
                skinInterface.apply(view);
            }
        }
    }

    /**
     *
     * @param context
     * @param attrs
     * @param name
     * @return
     */
    private View createView(Context context, AttributeSet attrs, String name) {
        try {
            Class clazz = context.getClassLoader().loadClass(name);
            Constructor<? extends View> constructor = clazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
            constructor.setAccessible(true);
            return constructor.newInstance(context, attrs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
