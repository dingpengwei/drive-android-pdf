package com.goodow.drive.android;

import android.content.Context;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.json.JsonObject;
import com.goodow.realtime.store.Store;
import com.google.inject.*;

/**
 * Created by dpw on 7/2/14.
 */
public class PdfModule extends AbstractModule {

    @Inject
    static Registry registry;


    @Provides
    @Singleton
    Bus provideBus(Store store) {
        return store.getBus();
    }
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
