package com.goodow.drive.android.pdf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by dpw on 7/10/14.
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = Uri.parse("/mnt/sdcard/git.pdf");
        Intent intent = new Intent(this,SimpleMuPdfActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        this.startActivity(intent);
        this.finish();
    }
}
