package com.goodow.drive.android;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.artifex.mupdf.MuPDFActivity;
import com.goodow.drive.android.pdf.PdfPlayer;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by dpw on 6/26/14.
 */
public class Registry {
    @Inject
    private Context ctx;
    @Inject
    private Bus bus;

    public void subscribe() {
      bus.subscribe(Constant.ADDR_PLAYER, new MessageHandler<JsonObject>() {
        @Override
        public void handle(Message<JsonObject> message) {
          JsonObject body = message.body();
          if (!body.has("path")) {
            return;
          }
          String path = body.getString("path");
          Intent intent = null;
          if (path.endsWith(".pdf")) {
            bus.sendLocal(Constant.ADDR_PLAYER_PDF_JZ, message.body(), null);
            return;
          } else {
            Toast.makeText(ctx, "不支持" + path, Toast.LENGTH_LONG).show();
            return;
          }
        }
      });
        bus.subscribeLocal(Constant.ADDR_PLAYER_PDF_JZ, new MessageHandler<JsonObject>() {
            @Override
            public void handle(Message<JsonObject> message) {
                Intent intent = new Intent(ctx, PdfPlayer.class);
                intent.putExtra("msg", message.body());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        bus.subscribeLocal(Constant.ADDR_PLAYER_PDF_MU, new MessageHandler<JsonObject>() {
            @Override
            public void handle(Message<JsonObject> message) {
                Intent intent = new Intent(ctx, MuPDFActivity.class);
                intent.putExtra("msg", message.body());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
    }
}
