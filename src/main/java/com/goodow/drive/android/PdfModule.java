package com.goodow.drive.android;

import android.content.Context;
import com.goodow.realtime.android.AndroidPlatform;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.channel.impl.WebSocketBus;
import com.goodow.realtime.java.JavaWebSocket;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonObject;
import com.goodow.realtime.store.Store;
import com.goodow.realtime.store.impl.StoreImpl;
import com.google.inject.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dpw on 7/2/14.
 */
public class PdfModule extends AbstractModule {
    private static final String SERVER = "ldh.goodow.com:1986";
    private static final String URL = "ws://" + SERVER + "/channel/websocket";

    @Inject
    static Registry registry;

    @Override
    protected void configure() {
        bind(Binder.class).asEagerSingleton();
    }


    static {
        AndroidPlatform.register();
        // adb shell setprop log.tag.JavaWebSocket DEBUG
        Logger.getLogger(JavaWebSocket.class.getName()).setLevel(Level.ALL);
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
    @Singleton
    public static class Binder {
        @Inject
        public Binder(final Provider<Bus> busProvider, final Provider<Context> context) {
            busProvider.get().subscribe("drive.svg", new MessageHandler<JsonObject>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    registry.subscribe();
                }
            });
        }
    }

}
