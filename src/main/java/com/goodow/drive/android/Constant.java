package com.goodow.drive.android;

import java.util.Arrays;
import java.util.List;

public interface Constant {

  String DRIVE = "drive";
  String ADDR_PLAYER = DRIVE + ".player";
  String ADDR_PLAYER_PDF_MU = ADDR_PLAYER + ".pdf.mu";
  String ADDR_PLAYER_PDF_JZ = ADDR_PLAYER + ".pdf.jz";
  String ADDR_CONTROL = DRIVE + ".control";
  String ADDR_DB = ADDR_PLAYER + ".analytics.db";

    List<String> ADDRESS_SET = Arrays.asList(new String[] {ADDR_CONTROL,ADDR_PLAYER, ADDR_PLAYER_PDF_MU, ADDR_PLAYER_PDF_JZ,ADDR_DB});
}
