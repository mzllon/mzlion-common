package com.mzlion.poi.constant;


import org.apache.poi.sl.usermodel.Hyperlink;

/**
 * Created by mzlion on 2016/6/13.
 */
public enum ExcelHyperLinkType {
    URL(Hyperlink.LINK_URL),
    EMAIL(Hyperlink.LINK_EMAIL),
    FILE(Hyperlink.LINK_FILE),;

    private final int type;

    ExcelHyperLinkType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
