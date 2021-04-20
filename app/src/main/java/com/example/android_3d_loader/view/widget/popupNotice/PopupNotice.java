package com.example.android_3d_loader.view.widget.popupNotice;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class PopupNotice {

    private Context mContext;
    private Toast mToast;
    private int mMillisecond;
    private boolean dismiss;
    private Handler mHandler = new Handler();

    private PopupNotice(Context context, Toast toast){
        this.mContext = context;
        this.mToast = toast;
    }

    public static PopupNotice makeInfo(Context context, String msg){

        PopupInfoNotice popupInfoNotice = new PopupInfoNotice(context);
        popupInfoNotice.setMsg(msg);
        Toast mToast = new Toast(context);
        mToast.setView(popupInfoNotice);
        mToast.setGravity(Gravity.TOP, 100, 100);
        mToast.setDuration(Toast.LENGTH_SHORT);

        return new PopupNotice(context, mToast);
    }

    public static PopupNotice makeWarning(Context context, String msg){

        PopupWarningNotice popupNotice = new PopupWarningNotice(context);
        popupNotice.setMsg(msg);
        Toast mToast = new Toast(context);
        mToast.setView(popupNotice);
        mToast.setGravity(Gravity.TOP, 100, 100);
        mToast.setDuration(Toast.LENGTH_SHORT);

        return new PopupNotice(context, mToast);
    }

    public static PopupNotice makeError(Context context, String msg){

        PopupErrorNotice popupNotice = new PopupErrorNotice(context);
        popupNotice.setMsg(msg);
        Toast mToast = new Toast(context);
        mToast.setView(popupNotice);
        mToast.setGravity(Gravity.TOP, 100, 100);
        mToast.setDuration(Toast.LENGTH_SHORT);

        return new PopupNotice(context, mToast);
    }

    public void show(){
        mToast.show();
    }

    public void show(int millisecond){
        if (!dismiss){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, Toast.LENGTH_LONG);
        }
    }

}
