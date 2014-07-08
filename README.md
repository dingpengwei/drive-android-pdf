drive-android-pdf [![Build Status](https://travis-ci.org/dingpengwei/drive-android-pdf.svg?branch=master)](https://travis-ci.org/dingpengwei/drive-android-pdf)
=================

**Get it in eclipse**.
```xml
<dependency>
  <groupId>com.goodow.drive</groupId>
  <artifactId>drive-android-pdf</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <type>apklib</type>
</dependency>
```
**Get it in idea**.
```xml
<dependency>
  <groupId>com.goodow.drive</groupId>
  <artifactId>drive-android-pdf</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <type>aar</type>
</dependency>
```
**Configure it in AndroidManifest.xml**.
```xml
<meta-data android:name="roboguice.modules" android:value="com.goodow.drive.android.PDFDriveAndroidModule,com.goodow.drive.android.PDFModule" />
<activity android:name="com.goodow.drive.android.pdf.MyJzPdfActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
<activity android:name="com.goodow.drive.android.pdf.MyMuPdfActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
```
**Control protocol**.
```yml
--- # @drive.player.pdf.jz 播放PDF
path: sample.pdf
--- # @drive.player.pdf.mu 播放PDF
path: sample.pdf
...

--- # @drive.player PDF播放器
path: /mnt/sdcard/sample.pdf # pdf文件路径
zoomTo: 3.0 # 指定缩放值, 基准是图片原始尺寸
zoomBy: 0.4 # 缩放至=当前缩放值*该缩放倍数 (0, .inf)
fit: 0 # 0表示适配屏幕(图片在屏幕上完全显示), 1表示宽度完全显示, 2表示高度完全显示
page: &page # 分页
  goTo: 9 # 跳转至该页, [1, .inf)
  move: 2 # 翻页, 负数向前, 正数向后
...
```
**Call in instruction**.
```javaScript
http://realtimeplayground.goodow.com/bus.html#server=http://realtime.goodow.com:1986/channel
open file:
bus.send("drive/your-mac",{"path":"drive.player.pdf.jz","msg":{"path":"/mnt/sdcard/ReferenceCard.pdf"}})
zoom:
bus.send("drive/your-mac",{"path":"drive.player","msg":{"zoomTo":"2.0"}})
bus.send("drive/your-mac",{"path":"drive.player","msg":{"zoomBy":0.5}})
page:
bus.send("drive/your-mac",{"path":"drive.player","msg":{"page":{"goTo":2}}})
bus.send("drive/your-mac",{"path":"drive.player","msg":{"page":{"move":-1}}})
fit:
bus.send("drive/your-mac",{"path":"drive.player","msg":{"fit":0}})
```
