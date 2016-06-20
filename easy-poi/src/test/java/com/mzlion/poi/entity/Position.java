package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;

import java.io.Serializable;

/**
 * Created by mzlion on 2016/6/15.
 */
@ExcelEntity
public class Position implements Serializable {

    private String id;

    @ExcelCell("职位名称")
    private String name;

    @ExcelCell("评职时间")
    private String createTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
