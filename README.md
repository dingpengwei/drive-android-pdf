drive-android-pdf [![Build Status](https://travis-ci.org/dingpengwei/drive-android-pdf.svg?branch=master)](https://travis-ci.org/dingpengwei/drive-android-pdf)
=================

```xml
<dependency>
  <groupId>com.goodow.realtime</groupId>
  <artifactId>realtime-json</artifactId>
  <version>0.5.5-SNAPSHOT</version>
</dependency>

<dependency>
  <groupId>com.goodow.drive</groupId>
  <artifactId>drive-android-pdf</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <type>aar</type>
</dependency>
```

```java
Intent intent = new Intent(ctx, MuPDFActivity.class);
intent.putExtra("msg", Json.createObject().set("path", "/mnt/sdcard/ReferenceCard.pdf").set("play", 1));
context.startActivity(intent);
```
