package com.goodow.drive.android.pdf.samples.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dpw on 7/10/14.
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pdf = this.getExternalCacheDir().getAbsolutePath() + "ReferenceCard.pdf";
        try{
            InputStream open = this.getAssets().open("ReferenceCard.pdf");
            OutputStream out = new FileOutputStream(new File(pdf));
            int len;
            byte[] buffer = new byte[1024];
            while((len = open.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();
            out.close();
            open.close();
        }catch (Exception e){

        }

        Uri uri = Uri.parse(pdf);
        Intent intent = new Intent(this,SimpleMuPdfActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        this.startActivity(intent);
        this.finish();
    }
}
