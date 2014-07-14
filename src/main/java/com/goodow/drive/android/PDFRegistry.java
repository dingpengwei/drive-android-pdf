package com.goodow.drive.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.goodow.drive.android.pdf.DriveAndroidMuPdfActivity;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;

import java.io.File;

/**
 * Created by dpw on 6/26/14.
 */
public class PDFRegistry {
    @Inject
    private Context ctx;
    @Inject
    private Bus bus;

    public void subscribe() {
//        bus.subscribe("drive/" + PDFDeviceInformationTools.getLocalMacAddressFromWifiInfo(ctx),
//                new MessageHandler<JsonObject>() {
//                    @Override
//                    public void handle(final Message<JsonObject> message) {
//                        JsonObject body = message.body();
//                        String path = body.getString("path");
//                        if (path != null && PDFConstant.ADDRESS_SET.contains(path)) {
//                            JsonObject msg = body.getObject("msg");
//                            bus.sendLocal(path, msg, new MessageHandler<JsonObject>() {
//                                @Override
//                                public void handle(Message<JsonObject> messageInner) {
//                                    JsonObject bodyInner = messageInner.body();
//                                    message.reply(bodyInner, null);
//                                }
//                            });
//                        } else {
//                            Toast.makeText(ctx, "address:" + path + "不存在", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

//      bus.subscribe(PDFConstant.ADDR_PLAYER, new MessageHandler<JsonObject>() {
//        @Override
//        public void handle(Message<JsonObject> message) {
//          JsonObject body = message.body();
//          if (!body.has("path")) {
//            return;
//          }
//          String path = body.getString("path");
//          if (path.endsWith(".pdf")) {
//            bus.sendLocal(PDFConstant.ADDR_PLAYER_PDF_JZ, message.body(), null);
//            return;
//          } else {
//            Toast.makeText(ctx, "不支持" + path, Toast.LENGTH_LONG).show();
//            return;
//          }
//        }
//      });


//        bus.subscribeLocal(PDFConstant.ADDR_PLAYER_PDF_MU, new MessageHandler<JsonObject>() {
//            @Override
//            public void handle(Message<JsonObject> message) {
//                try {
//                    Intent intent = new Intent(ctx, DriveAndroidMuPdfActivity.class);
//                    //intent.putExtra("msg", message.body());
//                    System.out.println(Uri.parse("file:/" + message.body().getString("path")).toString());
//                    File path = new File(message.body().getString("path"));
//                    intent.setData(Uri.parse(path.getAbsolutePath()));
//                intent.setAction(Intent.ACTION_VIEW);
//                    // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ctx.startActivity(intent);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
