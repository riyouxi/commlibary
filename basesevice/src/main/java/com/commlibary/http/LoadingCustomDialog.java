package com.commlibary.http;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import modle.test.com.basesevice.R;


public class LoadingCustomDialog extends Dialog {

    private static LoadingCustomDialog mLoadingProgress;
    public LoadingCustomDialog(Context context) {
        super(context);
    }

    public LoadingCustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static void showprogress(Context context,CharSequence message){
        mLoadingProgress = new LoadingCustomDialog(context,R.style.loading_dialog);
        mLoadingProgress.setCanceledOnTouchOutside(false);

        mLoadingProgress.setTitle("");
        mLoadingProgress.setContentView(R.layout.loading_dialog);
        if(message==null|| TextUtils.isEmpty(message)){
            mLoadingProgress.findViewById(R.id.loading_tv).setVisibility(View.GONE);
        }else {
            TextView tv = (TextView) mLoadingProgress.findViewById(R.id.loading_tv);
            tv.setText(message);
        }
        //按返回键响应是否取消等待框的显示
        mLoadingProgress.setCancelable(false);
        if(!mLoadingProgress.isShowing()){
            mLoadingProgress.show();
        }
    }

    public static void dismissprogress(){
        if(mLoadingProgress!=null){

            mLoadingProgress.dismiss();
        }
    }
}
