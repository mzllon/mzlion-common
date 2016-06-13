package com.mzlion.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by mzlion on 2016/6/13.
 */
public class AddImageExample {

    public static void main(String[] args) throws Exception {
                /* Create a Workbook and Worksheet */
        HSSFWorkbook my_workbook = new HSSFWorkbook();
        HSSFSheet my_sheet = my_workbook.createSheet("MyBanner");
                /* Read the input image into InputStream */
        InputStream my_banner_image = new FileInputStream("C:\\Users\\mzlion\\Pictures\\lookup_error.gif");
                /* Convert Image to byte array */
        byte[] bytes = IOUtils.toByteArray(my_banner_image);
                /* Add Picture to workbook and get a index for the picture */
        int my_picture_id = my_workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                /* Close Input Stream */
        my_banner_image.close();
                /* Create the drawing container */
        HSSFPatriarch drawing = my_sheet.createDrawingPatriarch();
                /* Create an anchor point */
        ClientAnchor my_anchor = new HSSFClientAnchor();
                /* Define top left corner, and we can resize picture suitable from there */
        my_anchor.setCol1(3);
        my_anchor.setRow1(3);
                /* Invoke createPicture and pass the anchor point and ID */
        HSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
                /* Call resize method, which resizes the image */
        my_picture.resize();
                /* Write changes to the workbook */
        FileOutputStream out = new FileOutputStream(new File("D:\\excel_insert_image_example.xls"));
        my_workbook.write(out);
        out.close();
    }
}
