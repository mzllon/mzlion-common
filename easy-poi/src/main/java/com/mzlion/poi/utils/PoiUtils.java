package com.mzlion.poi.utils;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

/**
 * Created by mzlion on 2016/6/8.
 */
public class PoiUtils {

    /**
     * 关闭Excel
     *
     * @param workbook Excel工作对象
     */
    public static void closeQuitely(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
