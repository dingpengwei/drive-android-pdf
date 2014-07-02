package com.goodow.drive.android;

import android.content.Context;

import com.goodow.realtime.channel.Bus;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Created by dpw on 7/2/14.
 */
public class PdfModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(Binder.class).asEagerSingleton();
    }

    @Singleton
    public static class Binder {
        @Inject
        public Binder(final Provider<Bus> busProvider, final Provider<Context> context , Registry registry) {
          registry.subscribe();
        }
    }

}
