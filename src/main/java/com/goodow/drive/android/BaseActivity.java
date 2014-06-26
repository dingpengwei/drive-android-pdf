package com.goodow.drive.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.State;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

/**
 * @title: BaseActivity.java
 * @package drive-android
 * @description: TODO
 * @author www.dingpengwei@gmail.com
 * @createDate 2013 2013-12-4 上午11:33:07
 * @updateDate 2013 2013-12-4 上午11:33:07
 * @version V1.0
 */
public class BaseActivity extends RoboActivity {

  @Inject
  public Bus bus;
  public SharedPreferences usagePreferences;
  public static final String USAGE_STATISTIC = "USAGE_STATISTIC";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    usagePreferences = getSharedPreferences(USAGE_STATISTIC, Context.MODE_MULTI_PROCESS);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (bus.getReadyState() == State.CLOSED || bus.getReadyState() == State.CLOSING) {
      Log.w("EventBus Status", bus.getReadyState().name());
    }
  }
}
