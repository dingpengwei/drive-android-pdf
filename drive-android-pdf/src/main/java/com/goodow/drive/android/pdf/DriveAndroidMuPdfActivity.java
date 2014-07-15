package com.goodow.drive.android.pdf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.artifex.mupdfdemo.*;
import com.goodow.drive.android.PDFConstant;
import com.goodow.drive.android.PDFDeviceInformationTools;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Key;
import roboguice.RoboGuice;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.EventManager;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dpw on 7/8/14.
 */
public class DriveAndroidMuPdfActivity extends MuPDFActivity implements FilePicker.FilePickerSupport, RoboContext {

    private MuPDFCore    core;
    private String       mFileName;
    private MuPDFReaderView mDocView;
    private int          mPageSliderRes;
    private AlertDialog.Builder mAlertBuilder;
    private boolean mAlertsActive= false;
    private AsyncTask<Void,Void,MuPDFAlert> mAlertTask;
    private AlertDialog mAlertDialog;


    protected EventManager eventManager;
    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    @Inject
    private Bus bus;
    private Registration controlHandler;
    private float currentScale;

    public void destroyAlertWaiter() {
        mAlertsActive = false;
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
        if (mAlertTask != null) {
            mAlertTask.cancel(true);
            mAlertTask = null;
        }
    }

    private MuPDFCore openFile(String path)
    {
        int lastSlashPos = path.lastIndexOf('/');
        mFileName = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos+1));
        System.out.println("Trying to open "+path);
        try
        {
            core = new MuPDFCore(this, path);
            // New file: drop the old outline data
            OutlineActivityData.set(null);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
        return core;
    }

    private MuPDFCore openBuffer(byte buffer[], String magic)
    {
        System.out.println("Trying to open byte buffer");
        try
        {
            core = new MuPDFCore(this, buffer, magic);
            // New file: drop the old outline data
            OutlineActivityData.set(null);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
        return core;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final RoboInjector injector = RoboGuice.getInjector(this);
        eventManager = injector.getInstance(EventManager.class);
        injector.injectMembersWithoutViews(this);
        eventManager.fire(new OnCreateEvent<Activity>(this,savedInstanceState));

        mAlertBuilder = new AlertDialog.Builder(this);

        if (core == null) {
            core = (MuPDFCore)getLastNonConfigurationInstance();

            if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
                mFileName = savedInstanceState.getString("FileName");
            }
        }
        if (core == null) {
            Intent intent = getIntent();
            byte buffer[] = null;
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                System.out.println("URI to open is: " + uri);
                if (uri.toString().startsWith("content://")) {
                    String reason = null;
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        int len = is.available();
                        buffer = new byte[len];
                        is.read(buffer, 0, len);
                        is.close();
                    }
                    catch (OutOfMemoryError e) {
                        System.out.println("Out of memory during buffer reading");
                        reason = e.toString();
                    }
                    catch (Exception e) {
                        System.out.println("Exception reading from stream: " + e);

                        // Handle view requests from the Transformer Prime's file manager
                        // Hopefully other file managers will use this same scheme, if not
                        // using explicit paths.
                        // I'm hoping that this case below is no longer needed...but it's
                        // hard to test as the file manager seems to have changed in 4.x.
                        try {
                            Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                            if (cursor.moveToFirst()) {
                                String str = cursor.getString(0);
                                if (str == null) {
                                    reason = "Couldn't parse data in intent";
                                }
                                else {
                                    uri = Uri.parse(str);
                                }
                            }
                        }
                        catch (Exception e2) {
                            System.out.println("Exception in Transformer Prime file manager code: " + e2);
                            reason = e2.toString();
                        }
                    }
                    if (reason != null) {
                        buffer = null;
                        Resources res = getResources();
                        AlertDialog alert = mAlertBuilder.create();
                        setTitle(String.format(res.getString(R.string.cannot_open_document_Reason), reason));
                        alert.setButton(AlertDialog.BUTTON_POSITIVE, "解除",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        alert.show();
                        return;
                    }
                }
                if (buffer != null) {
                    core = openBuffer(buffer, intent.getType());
                } else {
                    core = openFile(Uri.decode(uri.getEncodedPath()));
                }
                SearchTaskResult.set(null);
            }
            if (core != null && core.needsPassword()) {
                requestPassword(savedInstanceState);
                return;
            }
            if (core != null && core.countPages() == 0)
            {
                core = null;
            }
        }
        if (core == null)
        {
            AlertDialog alert = mAlertBuilder.create();
            alert.setTitle(R.string.cannot_open_document);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "解除",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            alert.show();
            return;
        }
        createUI(savedInstanceState);
    }

    public void requestPassword(final Bundle savedInstanceState) {
        AlertDialog alert = mAlertBuilder.create();
        alert.setTitle(R.string.enter_password);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alert.show();
    }

    public void createUI(Bundle savedInstanceState) {
        if (core == null)
            return;

        // Now create the UI.
        // First create the document view
        mDocView = new MuPDFReaderView(this) {
            @Override
            protected void onMoveToChild(int i) {
                if (core == null)
                    return;
                super.onMoveToChild(i);
            }
        };
        mDocView.setAdapter(new MuPDFPageAdapter(this, this, core));

        // Set up the page slider
        int smax = Math.max(core.countPages()-1,1);
        mPageSliderRes = ((10 + smax - 1)/smax) * 2;
        // Reenstate last state if it was recorded
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        mDocView.setDisplayedViewIndex(prefs.getInt("page"+mFileName, 0));

        // Stick the document view and the buttons overlay into a parent view
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(mDocView);
        setContentView(layout);
    }

    public Object onRetainNonConfigurationInstance()
    {
        MuPDFCore mycore = core;
        core = null;
        return mycore;
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
        eventManager.fire(new OnContentChangedEvent(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.controlHandler =
                bus.subscribeLocal(PDFConstant.ADDR_PLAYER, new MessageHandler<JsonObject>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        JsonObject body = message.body();
                        if (body.has("path")) {
                            return;
                        }
                        handleControlMessage(body);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        controlHandler.unregister();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            eventManager.fire(new OnStopEvent(this));
        } finally {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private float currentScaleX = 1.0f;
    private float currentScaleY = 1.0f;
    private void handleControlMessage(JsonObject body) {
        if (body.has("page")) {
            JsonObject page = body.getObject("page");
            if (page.has("goTo")) {
                /*
                 * goTo 指定页码的移动
                 */
                if (mDocView != null) {
                    mDocView.setDisplayedViewIndex((int) body.getNumber("goTo"));
                }
            } else if (page.has("move")) {
                /*
                 * move 相对于当前页码的偏移量移动
                 */
                if (mDocView != null) {
                    int offset = (mDocView.getDisplayedViewIndex() + (int) page.getNumber("move"));
                    mDocView.setDisplayedViewIndex(offset);
                }
            }
        } else if (body.has("zoomTo")) {
              /*
               * zoomTo 指定缩放数值,基数是1
               */
            if (mDocView != null) {
                float scale = (float) body.getNumber("zoomTo");
                mDocView.setScaleX(scale);
                mDocView.setScaleY(scale);
            }
        } else if (body.has("zoomBy")) {
              /*
               * zoomBy 指定缩放系数,基数是当前缩放值
               */
            if (mDocView != null) {
                currentScaleX = currentScaleX * (float) body.getNumber("zoomBy");
                currentScaleY = currentScaleY * (float) body.getNumber("zoomBy");
                mDocView.setScaleX(currentScaleX);
                mDocView.setScaleY(currentScaleY);
            }
        } else if(body.has("fit")){
            int fit = (int)body.getNumber("fit");
            if (mDocView != null) {
                int measuredWidth = mDocView.getDisplayedView().getMeasuredWidth();
                int measuredHeight = mDocView.getDisplayedView().getMeasuredHeight();
                int screenWidth = PDFDeviceInformationTools.getScreenWidth(this);
                int screenHeight = PDFDeviceInformationTools.getScreenHeight(this);

                float fitScaleX = (float) screenWidth / measuredWidth;
                float fitScaleY = (float) screenHeight / measuredHeight;
                switch(fit){
                    case 0:
                        mDocView.setScaleX(fitScaleX);
                        mDocView.setScaleY(fitScaleY);
                        break;
                    case 1:
                        mDocView.setScaleX(fitScaleX);
                        break;
                    case 2:
                        mDocView.setScaleY(fitScaleY);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
