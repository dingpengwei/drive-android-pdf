package com.goodow.drive.android;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;

import com.goodow.realtime.android.AndroidPlatform;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.impl.WebSocketBus;
import com.goodow.realtime.java.JavaWebSocket;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.store.Store;
import com.goodow.realtime.store.impl.StoreImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DriveAndroidPdfModule extends AbstractModule {
    private static final String SERVER = "realtime.goodow.com:1986";
    private static final String URL = "ws://" + SERVER + "/channel/websocket";

  static {
    AndroidPlatform.register();
    // adb shell setprop log.tag.JavaWebSocket DEBUG
    Logger.getLogger(JavaWebSocket.class.getName()).setLevel(Level.ALL);
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  Bus provideBus(Store store) {
    return store.getBus();
  }

  @Provides
  @Singleton
  Store provideStore(Provider<Context> contextProvider) {
      return new StoreImpl(URL, Json.createObject().set(WebSocketBus.SESSION, DeviceInformationTools
              .getLocalMacAddressFromWifiInfo(contextProvider.get())));
  }
}