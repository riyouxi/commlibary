package com.commlibary.utils;


import com.commlibary.download.helper.VersionUpdateImpl;

public class VersionUpdate {

    /**
     * 请求服务器，检查版本是否可以更新
     *
     * @param versionUpdate
     */
     public static void checkVersion(final VersionUpdateImpl versionUpdate) {
         //从网络请求获取到的APK下载路径，此处是随便找的链接
         versionUpdate.bindService("http://pic.to8to.com/app/android/to8to/20170520_cfa373eb3561ac8f6e5eXjSgDqC2vZOY.apk");
     }
}
