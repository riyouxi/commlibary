package com.commlibary.utils;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 管理Android Activity队列栈,单利存在
 * 统一管理移除结束和Activity队列栈中的数量控制等
 */
public class ActivityTaskManager {
    private static final ActivityTaskManager instance = new ActivityTaskManager();//Activity单例队列栈
    private static ArrayList<Activity> mList = new ArrayList<>();//保持所有未被销毁的Activity集合

    //私有构造方法，防止外部创建对象
    private ActivityTaskManager() {
    }

    public static ActivityTaskManager create() {
        return instance;
    }

    /**
     * 添加Activity
     *
     * @param mActivity 待添加的Activity
     */
    public void addActivity2Task(Activity mActivity) {
        mList.add(mActivity);
    }

    /**
     * 移除Activity,如果该Activity在栈中存在多个，只是移除最后一个
     *
     * @param mActivity 待移除的Activity
     */
    public void destoryActivity4Task(Activity mActivity) {
        if (mList.size() > 0 && mList.get(mList.size() - 1) == mActivity) {
            mList.remove(mList.size() - 1);
        }
    }

    /**
     * 获取传递的Activity是否是前台显示的Activity。
     * 判断规则是：所有Activity都是添加到mList集合中的，当前activity肯定位于最后一个
     */
    public boolean isActivityForeground(Activity mActivity) {
        if (mList != null && mList.size() > 0 && mActivity != null) {
            if (TextUtils.equals(mList.get(mList.size() - 1).getClass().getName(), mActivity.getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束Activity队列中所有Activity
     * 一键退出多个Activity或者类似返回主界面功能是可使用
     */
    public void finishAllActivity() {
        if (mList != null && !mList.isEmpty()) {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mList.clear();
        }
    }

    /**
     * 退出程序的时候调用
     */
    public void appExit() {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
}
