package com.goodow.drive.android.pdf;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.goodow.drive.android.PDFBaseActivity;
import com.goodow.drive.android.PDFConstant;
import com.goodow.drive.android.PDFDeviceInformationTools;
import com.goodow.drive.android.R;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

/**
 * @title: SamplePDF.java
 * @package drive-android
 * @description: PDF阅读器调用示例
 * @author www.dingpengwei@gmail.com
 * @createDate 2013 2013-12-4 上午10:48:34
 * @updateDate 2013 2013-12-4 上午10:48:34
 * @version V1.0
 */
public class MyJzPdfActivity extends PDFBaseActivity implements OnClickListener, OnLoadCompleteListener,
        OnPageChangeListener, OnDrawListener {
    private PDFView pdfView;
    private float currentScale = 2.4f;
    private int currentPage = 0;
    private Registration controlHandler;
    @Inject
    private Bus bus;
    /*
     * override PDFVIEW LIB invoke when page load complete
     */
    @Override
    public void loadComplete(int nbPages) {
        float pdfViewWidth = pdfView.getOptimalPageWidth();
        int screenWidth = PDFDeviceInformationTools.getScreenWidth(this);
        float fitScale = (float) screenWidth / pdfViewWidth;
        pdfView.setScaleX(fitScale);
        pdfView.zoomCenteredTo(1.0f, new PointF(PDFDeviceInformationTools.getScreenWidth(MyJzPdfActivity.this) / 2, 0));
        pdfView.loadPages();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View) 处理屏幕按钮点击事件
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bu_pdf_pre_page){
            bus.sendLocal(PDFConstant.ADDR_PLAYER, Json.createObject().set("page", Json.createObject().set("move", -1)), null);
        }
        if(id == R.id.bu_pdf_next_page){
            bus.sendLocal(PDFConstant.ADDR_PLAYER, Json.createObject().set("page", Json.createObject().set("move", 1)), null);
        }
        if(id == R.id.bu_pdf_max){
            bus.sendLocal(PDFConstant.ADDR_PLAYER, Json.createObject().set("zoomBy", 1.2), null);
        }
        if(id == R.id.bu_pdf_min){
            bus.sendLocal(PDFConstant.ADDR_PLAYER, Json.createObject().set("zoomBy", 0.8), null);
        }
        if(id == R.id.iv_back){
            bus.sendLocal(PDFConstant.ADDR_DB,null,null);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        bus.sendLocal(PDFConstant.ADDR_DB, null, null);
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        pdfView.zoomTo(this.currentScale);
        super.onConfigurationChanged(newConfig);
    }

    /*
     * override PDFVIEW LIB invoke when draw
     */
    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
    }

    /*
     * override PDFVIEW LIB invoke when page change
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        this.currentPage = page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        this.buildPdfView(this.getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.buildPdfView(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        controlHandler.unregister();
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = savedInstanceState.getInt("currentPage");
                currentScale = savedInstanceState.getInt("currentScale");
                pdfView.jumpTo(savedInstanceState.getInt("currentPage"));
                pdfView.loadPages();
            }
        }, 200);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlHandler =
                bus.subscribeLocal(PDFConstant.ADDR_PLAYER, new MessageHandler<JsonObject>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        JsonObject body = message.body();
                        if (body.has("path")) {
                            return;
                        }
                        currentScale = pdfView.getZoom();
                        if (body.has("zoomTo")) {
                            if (pdfView != null) {
                                currentScale = (float) body.getNumber("zoomTo");
                                pdfView.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools
                                        .getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                pdfView.loadPages();
                            }
                        }
                        if (body.has("zoomBy")) {
                            if (pdfView != null && (float) body.getNumber("zoomBy") * currentScale < 10
                                    && (float) body.getNumber("zoomBy") * currentScale > 0.1) {
                                currentScale = (float) body.getNumber("zoomBy") * currentScale;
                                pdfView.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools
                                        .getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                pdfView.loadPages();
                            }
                        }

                        if (body.has("page")) {
                            JsonObject pdfControl = body.getObject("page");
                            if (pdfControl.has("goTo")) {
                                /*
                                 * goTo 指定页码的移动
                                 */
                                if (pdfView != null) {
                                    pdfView.jumpTo((int) pdfControl.getNumber("goTo"));
                                    pdfView.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools
                                            .getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                    pdfView.loadPages();
                                }
                            }
                            if (pdfControl.has("move")) {
                                /*
                                 * move 相对于当前页码的偏移量移动
                                 */
                                if (pdfView != null) {
                                    pdfView.jumpTo(pdfView.getCurrentPage() + 1 + (int) pdfControl.getNumber("move"));
                                    pdfView.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools
                                            .getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                    pdfView.loadPages();
                                }
                            }
                        }

                        if(body.has("fit")){
                            int fit = (int)body.getNumber("fit");
                            float pdfViewWidth = pdfView.getOptimalPageWidth();
                            int screenWidth = PDFDeviceInformationTools.getScreenWidth(MyJzPdfActivity.this);
                            float fitScaleX = (float) screenWidth / pdfViewWidth;
                            switch(fit){
                                case 0:
                                    pdfView.setScaleX(fitScaleX);
//                                    pdfView.setScaleY(fitScaleX);
                                    pdfView.zoomCenteredTo(1.0f, new PointF(PDFDeviceInformationTools.getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                    pdfView.loadPages();
                                    break;
                                case 1:
                                    pdfView.setScaleX(fitScaleX);
                                    pdfView.zoomCenteredTo(1.0f, new PointF(PDFDeviceInformationTools.getScreenWidth(MyJzPdfActivity.this) / 2, 0));
                                    pdfView.loadPages();
                                    break;
                                case 2:
//                                    pdfView.setScaleY(fitScaleX);
//                                    pdfView.zoomCenteredTo(1.0f, new PointF(DeviceInformationTools.getScreenWidth(PdfPlayer.this) / 2, 0));
//                                    pdfView.loadPages();
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat("currentScale", this.currentScale);
        outState.putInt("currentPage", this.currentPage);
        super.onSaveInstanceState(outState);
    }

    /*
     * 加载文档
     */
    private void buildPdfView(Intent intent) {
        JsonObject jsonObject = (JsonObject) intent.getExtras().getSerializable("msg");
        File newFile = new File(jsonObject.getString("path"));
        if (newFile.exists()) {
            pdfView.fromFile(newFile).defaultPage(1).onLoad(this).onDraw(this).onPageChange(this).onLoad( this).load();
        } else {
            Toast.makeText(this, this.getString(R.string.pdf_file_no_exist), Toast.LENGTH_SHORT).show();
        }
    }

    public void hiddenToolBar(){
        this.findViewById(R.id.ll_tool_bar).setVisibility(View.GONE);
    }

    public void showToolBar(){
        this.findViewById(R.id.ll_tool_bar).setVisibility(View.VISIBLE);
    }

    public void hiddenBackButton(){
        this.findViewById(R.id.iv_back).setVisibility(View.GONE);
    }

    public void showBackButton(){
        this.findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
    }
}
