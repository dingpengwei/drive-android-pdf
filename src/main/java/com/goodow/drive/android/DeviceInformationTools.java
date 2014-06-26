package com.goodow.drive.android;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;

/**
 * Created by dpw on 5/22/14.
 */
public class DeviceInformationTools {

  /*
   * 获取屏幕高度
   */
  public static int getScreenHeight(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    int height = dm.heightPixels;
    return height;
  }

  /*
   * 获取屏幕宽度
   */
  public static int getScreenWidth(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    return width;
  }


    // 根据Wifi信息获取本地Mac
  public static String getLocalMacAddressFromWifiInfo(Context context) {
        String mac = null;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            mac = mWifiInfo.getMacAddress();
        } else {
            mWifiManager.setWifiEnabled(true);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            mac = mWifiInfo.getMacAddress();
            mWifiManager.setWifiEnabled(false);
        }
        return mac;
    }
}
