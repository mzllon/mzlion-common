package com.mzlion.poi;

import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.config.PropertyCellMapConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by mzlion on 2016/6/12.
 */
public class ExcelMapClassTest {

    private static List<Map<String, String>> cityZjsList = new ArrayList<>();
    private static List<PropertyCellMapConfig> propertyCellMapConfigList = new ArrayList<>();

    @BeforeClass
    public static void setUp() throws Exception {
        Map<String, String> city;

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3310");
        city.put("cityName", "杭州市");
        city.put("spellFull", "hangzhoushi");
        city.put("spellSimple", "HZS");
        city.put("zipCode", "310000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3320");
        city.put("cityName", "宁波市");
        city.put("spellFull", "ningboshi");
        city.put("spellSimple", "NBS");
        city.put("zipCode", "315000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3330");
        city.put("cityName", "温州市");
        city.put("spellFull", "wenzhoushi");
        city.put("spellSimple", "WZS");
        city.put("zipCode", "325000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3350");
        city.put("cityName", "嘉兴市");
        city.put("spellFull", "jiaxingshi");
        city.put("spellSimple", "JXS");
        city.put("zipCode", "314000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3360");
        city.put("cityName", "湖州市");
        city.put("spellFull", "huzhoushi");
        city.put("spellSimple", "HZS");
        city.put("zipCode", "313000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3370");
        city.put("cityName", "绍兴市");
        city.put("spellFull", "shaoxingshi");
        city.put("spellSimple", "SXS");
        city.put("zipCode", "312000");
        cityZjsList.add(city);

        city = new HashMap<>(8);
        city.put("provId", "33");
        city.put("cityId", "3380");
        city.put("cityName", "金华市");
        city.put("spellFull", "jinhuashi");
        city.put("spellSimple", "JHS");
        city.put("zipCode", "321000");
        cityZjsList.add(city);

        propertyCellMapConfigList.addAll(Arrays.asList(
                new PropertyCellMapConfig.Builder()
                        .title("省份编号")
                        .propertyName("provId")
                        .build(),
                new PropertyCellMapConfig.Builder()
                        .title("城市编号")
                        .propertyName("cityId")
                        .build(),
                new PropertyCellMapConfig.Builder()
                        .title("城市名称")
                        .propertyName("cityName")
                        .build(),
                new PropertyCellMapConfig.Builder()
                        .title("全拼")
                        .propertyName("spellFull")
                        .build(),
                new PropertyCellMapConfig.Builder()
                        .title("简拼")
                        .propertyName("spellSimple")
                        .build(),
                new PropertyCellMapConfig.Builder()
                        .title("邮政编码")
                        .propertyName("zipCode")
                        .build()
        ));
    }

    @Test
    public void write() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource(".");
        String resourceFile = resource.getFile();
        File output = new File(resourceFile, "export/cityZjsList.xlsx");
        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .title("浙江省城市代码表")
                .sheetName("浙江省城市代码表")
                .mapConfig(propertyCellMapConfigList)
                .build();
        ExcelUtils.write(cityZjsList, excelWriteConfig, output);
        assertTrue(output.exists());
        System.out.println(output.getCanonicalPath());
    }
}
