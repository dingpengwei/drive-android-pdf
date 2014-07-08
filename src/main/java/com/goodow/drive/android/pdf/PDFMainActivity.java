package com.goodow.drive.android.pdf;

import android.os.Bundle;
import com.goodow.drive.android.PDFBaseActivity;
import com.goodow.drive.android.PDFConstant;
import com.goodow.drive.android.PDFRegistry;
import com.goodow.realtime.json.Json;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dpw on 6/26/14.
 */
public class PDFMainActivity extends PDFBaseActivity {
    @Inject
    private PDFRegistry registry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registry.subscribe();
        try {
            InputStream open = this.getAssets().open("ReferenceCard.pdf");
            OutputStream outputStream = new FileOutputStream(new File("/mnt/sdcard/ReferenceCard.pdf"));
            int len;
            byte[] buffer = new byte[1024];
            while((len = open.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }
            outputStream.flush();
            outputStream.close();
            open.close();
        }catch (Exception e){

        }
        bus.sendLocal(PDFConstant.ADDR_PLAYER, Json.createObject().set("path", "/mnt/sdcard/ref.pdf"), null);
        this.finish();
    }
}
