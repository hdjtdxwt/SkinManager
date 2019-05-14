package com.epsit.skinmanager;

import android.view.View;

public abstract class SkinInterface {
    /**
     * 引入名字  background或者textColor
     */
    protected String attrName = null;

    //id
    protected int refId = 0;
    /**
     * resource name资源名字比如 @drawable/background_1
     */
    protected String attrValueName = null;

    /**
     * 类型  color 和 drawable  (drawable主要针对background，color主要针对textColor)
     */
    protected String attrType = null;

    public SkinInterface(String attrName, int refId, String attrValueName,String attrType){
        this.attrName = attrName;
        this.refId = refId;
        this.attrValueName = attrValueName;
        this.attrType = attrType;
    }

    /**
     * 应用皮肤
     * @param view
     */
    public abstract  void apply(View view);
}
