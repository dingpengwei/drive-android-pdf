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

    @Inject
    static Registry registry;

    @Override
    protected void configure() {
        bind(Binder.class).asEagerSingleton();
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
