package com.epsit.skinmanager;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BackgroundSkin extends SkinInterface {
    String TAG ="BackgroundSkin";
    public BackgroundSkin(String attrName, int refId, String attrValueName, String attrType) {
        //@2131099746
        super(attrName, refId, attrValueName, attrType);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void apply(View view) {
        Log.e(TAG,"View切换背景颜色  refId="+refId);
        if(attrType.equals("color")){
            int color = SkinManager.getInstance().getColor(refId);
            view.setBackgroundColor(color);
        }else if(attrType.equals("drawable")){
            Drawable drawable = SkinManager.getInstance().getDrawable(refId);
            view.setBackground(drawable);
        }

    }
}
