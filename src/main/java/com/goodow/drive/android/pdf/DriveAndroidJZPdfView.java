package com.goodow.drive.android.pdf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import com.goodow.drive.android.PDFConstant;
import com.goodow.drive.android.PDFDeviceInformationTools;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.model.PagePart;
import org.vudroid.core.DecodeService;

import java.io.File;

/**
 * Created by dpw on 7/8/14.
 */
public class DriveAndroidJZPdfView extends PDFView {

    @Inject
    private Bus bus;
    private Registration controlHandler;
    private float currentScale;
    private Context context;

    public DriveAndroidJZPdfView(Context context, AttributeSet set) {
        super(context, set);
        this.context = context;
    }

    @Override
    public void jumpTo(int page) {
        super.jumpTo(page);
    }

    @Override
    public int getPageCount() {
        return super.getPageCount();
    }

    @Override
    public void enableSwipe(boolean enableSwipe) {
        super.enableSwipe(enableSwipe);
    }

    @Override
    public void recycle() {
        super.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onLayerUpdate() {
        super.onLayerUpdate();
    }

    @Override
    public void loadPages() {
        super.loadPages();
    }

    @Override
    public void loadComplete(DecodeService decodeService) {
        super.loadComplete(decodeService);
    }

    @Override
    public void onBitmapRendered(PagePart part) {
        super.onBitmapRendered(part);
    }

    @Override
    public void moveTo(float offsetX, float offsetY) {
        super.moveTo(offsetX, offsetY);
    }

    @Override
    public void moveRelativeTo(float dx, float dy) {
        super.moveRelativeTo(dx, dy);
    }

    @Override
    public void zoomTo(float zoom) {
        super.zoomTo(zoom);
    }

    @Override
    public void zoomCenteredTo(float zoom, PointF pivot) {
        super.zoomCenteredTo(zoom, pivot);
    }

    @Override
    public void zoomCenteredRelativeTo(float dzoom, PointF pivot) {
        super.zoomCenteredRelativeTo(dzoom, pivot);
    }

    @Override
    public int getCurrentPage() {
        return super.getCurrentPage();
    }

    @Override
    public float getCurrentXOffset() {
        return super.getCurrentXOffset();
    }

    @Override
    public float getCurrentYOffset() {
        return super.getCurrentYOffset();
    }

    @Override
    public float toRealScale(float size) {
        return super.toRealScale(size);
    }

    @Override
    public float toCurrentScale(float size) {
        return super.toCurrentScale(size);
    }

    @Override
    public float getZoom() {
        return super.getZoom();
    }

    @Override
    public boolean isZooming() {
        return super.isZooming();
    }

    @Override
    public float getOptimalPageWidth() {
        return super.getOptimalPageWidth();
    }

    @Override
    public void resetZoom() {
        super.resetZoom();
    }

    @Override
    public void resetZoomWithAnimation() {
        super.resetZoomWithAnimation();
    }

    @Override
    public Configurator fromAsset(String assetName) {
        return super.fromAsset(assetName);
    }

    @Override
    public Configurator fromFile(File file) {
        return super.fromFile(file);
    }

    @Override
    public SurfaceHolder getHolder() {
        return super.getHolder();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean gatherTransparentRegion(Region region) {
        return super.gatherTransparentRegion(region);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        super.setZOrderMediaOverlay(isMediaOverlay);
    }

    @Override
    public void setZOrderOnTop(boolean onTop) {
        super.setZOrderOnTop(onTop);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.controlHandler =
                bus.subscribeLocal(PDFConstant.ADDR_PLAYER, new MessageHandler<JsonObject>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        JsonObject body = message.body();
                        if (body.has("path")) {
                            return;
                        }
                        currentScale = DriveAndroidJZPdfView.this.getZoom();
                        if (body.has("zoomTo")) {
                            if (DriveAndroidJZPdfView.this != null) {
                                currentScale = (float) body.getNumber("zoomTo");
                                DriveAndroidJZPdfView.this.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                DriveAndroidJZPdfView.this.loadPages();
                            }
                        }
                        if (body.has("zoomBy")) {
                            if (DriveAndroidJZPdfView.this != null && (float) body.getNumber("zoomBy") * currentScale < 10
                                    && (float) body.getNumber("zoomBy") * currentScale > 0.1) {
                                currentScale = (float) body.getNumber("zoomBy") * currentScale;
                                DriveAndroidJZPdfView.this.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                DriveAndroidJZPdfView.this.loadPages();
                            }
                        }

                        if (body.has("page")) {
                            JsonObject pdfControl = body.getObject("page");
                            if (pdfControl.has("goTo")) {
                                /*
                                 * goTo 指定页码的移动
                                 */
                                if (DriveAndroidJZPdfView.this != null) {
                                    DriveAndroidJZPdfView.this.jumpTo((int) pdfControl.getNumber("goTo"));
                                    DriveAndroidJZPdfView.this.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                    DriveAndroidJZPdfView.this.loadPages();
                                }
                            }
                            if (pdfControl.has("move")) {
                                /*
                                 * move 相对于当前页码的偏移量移动
                                 */
                                if (DriveAndroidJZPdfView.this != null) {
                                    DriveAndroidJZPdfView.this.jumpTo(DriveAndroidJZPdfView.this.getCurrentPage() + 1 + (int) pdfControl.getNumber("move"));
                                    DriveAndroidJZPdfView.this.zoomCenteredTo(currentScale, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                    DriveAndroidJZPdfView.this.loadPages();
                                }
                            }
                        }

                        if(body.has("fit")){
                            int fit = (int)body.getNumber("fit");
                            float pdfViewWidth = DriveAndroidJZPdfView.this.getOptimalPageWidth();
                            int screenWidth = PDFDeviceInformationTools.getScreenWidth(context);
                            float fitScaleX = (float) screenWidth / pdfViewWidth;
                            switch(fit){
                                case 0:
                                    DriveAndroidJZPdfView.this.setScaleX(fitScaleX);
//                                    MyJZPdfView.this.setScaleY(fitScaleX);
                                    DriveAndroidJZPdfView.this.zoomCenteredTo(1.0f, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                    DriveAndroidJZPdfView.this.loadPages();
                                    break;
                                case 1:
                                    DriveAndroidJZPdfView.this.setScaleX(fitScaleX);
                                    DriveAndroidJZPdfView.this.zoomCenteredTo(1.0f, new PointF(PDFDeviceInformationTools.getScreenWidth(context) / 2, 0));
                                    DriveAndroidJZPdfView.this.loadPages();
                                    break;
                                case 2:
//                                    MyJZPdfView.this.setScaleY(fitScaleX);
//                                    MyJZPdfView.this.zoomCenteredTo(1.0f, new PointF(DeviceInformationTools.getScreenWidth(PdfPlayer.this) / 2, 0));
//                                    MyJZPdfView.this.loadPages();
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.controlHandler.unregister();
    }
}
