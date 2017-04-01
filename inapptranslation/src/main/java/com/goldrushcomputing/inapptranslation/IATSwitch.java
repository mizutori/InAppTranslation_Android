package com.goldrushcomputing.inapptranslation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

/**
 * Created by Takamitsu Mizutori on 2017/04/01.
 */

public class IATSwitch extends Switch {

    public IATSwitch(){
        super(null);
    }

    public IATSwitch(Context context){
        super(context);
    }

    public IATSwitch(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public IATSwitch(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }


    @Override
    public void requestLayout() {
        try {
            java.lang.reflect.Field mOnLayout = Switch.class.getDeclaredField("mOnLayout");
            mOnLayout.setAccessible(true);
            mOnLayout.set(this, null);
            java.lang.reflect.Field mOffLayout = Switch.class.getDeclaredField("mOffLayout");
            mOffLayout.setAccessible(true);
            mOffLayout.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.requestLayout();
    }
}
