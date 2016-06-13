package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelHyperLink;
import com.mzlion.poi.constant.ExcelCellType;

/**
 * Created by mzlion on 2016/6/13.
 */
@ExcelEntity
public class WebSite {

    @ExcelCell("网站编号")
    private String id;

    @ExcelCell("网站名称")
    private String name;

    @ExcelCell(value = "网站地址", type = ExcelCellType.HYPER_LINK, width = 23)
    @ExcelHyperLink
    private String url;

    @ExcelCell(value = "描述", autoWrap = true, width = 30)
    private String desc;

    public WebSite() {
    }

    public WebSite(String id, String name, String url, String desc) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebSite{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
