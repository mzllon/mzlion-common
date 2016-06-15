package com.mzlion.poi;

import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.entity.WebSite;
import com.mzlion.poi.excel.ExcelUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by mzlion on 2016/6/13.
 */
public class ExcelHyperlinkExportTest {

    private static List<WebSite> webSiteList;


    @BeforeClass
    public static void setUp() throws Exception {
        webSiteList = new ArrayList<>(4);
        WebSite webSite;

        webSite = new WebSite("baidu", "百度", "https://www.baidu.com", "百度（Nasdaq：BIDU）是全球最大的中文搜索引擎、最大的中文网站。2000年1月由李彦宏创立于北京中关村，致力于向人们提供“简单，可依赖”的信息获取方式。“百度”二字源于中国宋朝词人辛弃疾的《青玉案·元夕》词句“众里寻他千百度”，象征着百度对中文信息检索技术的执著追求。");
        webSiteList.add(webSite);

        webSite = new WebSite("google", "谷歌", "https://wwww.google.com", "Google（中文名：谷歌），是一家美国的跨国科技企业，致力于互联网搜索、云计算、广告技术等领域，开发并提供大量基于互联网的产品与服务，其主要利润来自于AdWords等广告服务。Google由当时在斯坦福大学攻读理工博士的拉里·佩奇和谢尔盖·布卢姆共同创建，因此两人也被称为“Google Guys”。");
        webSiteList.add(webSite);

    }

    @Test
    public void export() throws Exception {
        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .beanClass(WebSite.class)
                .build();
        URL resource = this.getClass().getClassLoader().getResource(".");
        String resourceFile = resource.getFile();
        File output = new File(resourceFile, "export/website_hyperlink.xlsx");
        output.delete();
        ExcelUtils.write(webSiteList, excelWriteConfig, output);
        assertTrue(output.exists());
    }
}
