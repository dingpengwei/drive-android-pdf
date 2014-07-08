package com.goodow.drive.android.pdf;

import android.os.Bundle;
import android.view.Menu;
import com.artifex.mupdfdemo.MuPDFActivity;

/**
 * Created by dpw on 7/8/14.
 */
public class MyMuPdfActivity extends MuPDFActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        return false;
    }

}
