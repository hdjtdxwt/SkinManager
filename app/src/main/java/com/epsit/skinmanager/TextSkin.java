package com.epsit.skinmanager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextSkin extends SkinInterface{
    String TAG ="TextSkin";
    public TextSkin(String attrName, int refId, String attrValueName, String attrType) {
        super(attrName, refId, attrValueName, attrType);
    }

    /**
     * 设置对应的属性
     * @param view
     */
    @Override
    public void apply(View view) {
        if(view instanceof TextView){
            TextView textView = (TextView) view;
            Log.e(TAG,"textView切换字体颜色");
            textView.setTextColor(SkinManager.getInstance().getColor(refId));
        }else if(view instanceof Button){
            Button button = (Button) view;
            Log.e(TAG,"button切换字体颜色");
            button.setTextColor(SkinManager.getInstance().getColor(refId));
        }
    }
}
