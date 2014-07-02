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
<meta-data android:name="roboguice.modules" android:value="com.goodow.drive.android.PdfModule" />
<activity android:name="com.artifex.mupdf.MuPDFActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
<activity android:name="com.goodow.drive.android.pdf.PdfPlayer" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"/>
```

```yml
--- # @drive.player.pdf.jz
path: sample.pdf
--- # @drive.player.pdf.mu
path: sample.pdf
...
```

```java
Intent intent = new Intent(ctx, MuPDFActivity.class);
intent.putExtra("msg", Json.createObject().set("path", "/mnt/sdcard/ReferenceCard.pdf").set("play", 1));
context.startActivity(intent);
```
