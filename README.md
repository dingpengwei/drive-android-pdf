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
<meta-data android:name="roboguice.modules" android:value="com.goodow.drive.android.DriveAndroidModule,com.goodow.drive.android.PdfModule" />
<activity android:name="com.artifex.mupdf.MuPDFActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
<activity android:name="com.goodow.drive.android.pdf.PdfPlayer" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
```
**Control protocol**.
```yml
--- # @drive.player.pdf.jz 播放PDF
path: sample.pdf
--- # @drive.player.pdf.mu 播放PDF
path: sample.pdf
...

--- # @drive.player 播放器
path: sample.pdf # 路径 
zoomTo: 3.0 # 指定缩放值, 基准是原始尺寸
zoomBy: 0.4 # 缩放至=当前缩放值*该缩放倍数 (0, .inf)
page: &page # 分页
  goTo: 9 # 跳转至该页, [1, .inf)
  move: 2 # 翻页, 负数向前, 正数向后
...
```

```java
bus.sendLocal("drive.player.pdf.jz",Json.createObject().set("path", "/mnt/sdcard/ReferenceCard.pdf").set("play", 1),null);
bus.sendLocal("drive.player.pdf.mu",Json.createObject().set("path", "/mnt/sdcard/ReferenceCard.pdf").set("play", 1),null);
```
