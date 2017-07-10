package modle.test.com.commlibary.base;

import android.app.Application;
import android.content.Context;

import com.commlibary.download.widget.DisplayToast;
import com.commlibary.utils.SPUtil;


public class BaseApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //初始化Toast
        DisplayToast.getInstance().init(getApplicationContext());
        SPUtil.init(getApplicationContext());
    }

    /**
     * 获取上下文
     * @return Context
     */
    public static Context getContext() {
        return context;
    }
}
