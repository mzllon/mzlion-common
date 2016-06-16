package com.mzlion.poi.excel;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.reflect.StaticFieldFilter;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelId;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.config.ExcelCellConfig;
import com.mzlion.poi.config.ExcelReadConfig;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.exception.BeanNotConfigAnnotationException;
import com.mzlion.poi.exception.ExcelCellConfigException;
import com.mzlion.poi.exception.ExcelImportException;
import com.mzlion.poi.exception.ExcelReadException;
import com.mzlion.poi.utils.PoiUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by mzlion on 2016/6/8.
 */
class ExcelReaderEngine<T> {
    private Logger logger = LoggerFactory.getLogger(ExcelReaderEngine.class);

    /**
     * 导入的配置选项
     */
    final ExcelReadConfig excelReadConfig;

    private ExcelType excelType = ExcelType.XLSX;
    private List<InternalReadExcelCellConfig> internalReadExcelCellConfigList = new ArrayList<>();

    List<ExcelCellConfig> excelCellConfigs;

    private ExcelCellConfig excelId_ExcelCellConfig;

    ExcelReaderEngine(ExcelReadConfig excelReadConfig) {
        this.excelReadConfig = excelReadConfig;

        if (ClassUtils.isAssignable(Map.class, this.excelReadConfig.getRawClass())) {
            this.excelCellConfigs = this.excelReadConfig.getExcelCellConfigList();
        } else {
            List<ExcelCellConfig> parseExcelCellConfigList = this.parseExcelCellConfigList(this.excelReadConfig.getRawClass(), null);
            if (CollectionUtils.isEmpty(parseExcelCellConfigList)) {
                throw new ExcelCellConfigException("The class[" + this.excelReadConfig.getRawClass() + "] does't config any `ExcelCell` in properties");
            }
            this.excelCellConfigs = parseExcelCellConfigList;
        }
    }

    List<T> read(InputStream excelInputStream) {
        Workbook workbook = null;
        try {
            if (!excelInputStream.markSupported()) excelInputStream = new PushbackInputStream(excelInputStream, 8);
            if (POIFSFileSystem.hasPOIFSHeader(excelInputStream)) {
                excelType = ExcelType.XLS;
                workbook = new HSSFWorkbook(excelInputStream);
            } else if (POIXMLDocument.hasOOXMLHeader(excelInputStream)) {
                workbook = new XSSFWorkbook(OPCPackage.open(excelInputStream));
            } else {
                throw new ExcelReadException("InputStream was not MS ExcelEntity stream.");
            }

            //获取公式解析器
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            //记录解析开始时间
            long startTime = System.currentTimeMillis();
            List<T> dataSet = new ArrayList<>();
            //按照sheet读取
            for (int i = this.excelReadConfig.getSheetIndex() - 1; i < this.excelReadConfig.getSheetNum(); i++) {
                logger.debug(" ===> Reading sheet at index[" + i + "]");
                dataSet.addAll(this.parseSheet(workbook.getSheetAt(i), formulaEvaluator));
            }
            logger.debug(" ===> ExcelEntity import process finish,it cost time is {} milliseconds", (System.currentTimeMillis() - startTime));
            return dataSet;
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelImportException("Reading excel failed.", e);
        } finally {
            PoiUtils.closeQuitely(workbook);
        }
    }


    private List<ExcelCellConfig> parseExcelCellConfigList(Class<?> rawClass, String[] propertyNames) {
        List<ExcelCellConfig> parseExcelCellConfigList = new ArrayList<>();
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(rawClass);
        ExcelCell excelCell;
        ExcelCellConfig.Builder builder;
        ExcelMappedEntity excelMappedEntity;

        //如果属性标有ExcelMappedEntity则必须指定某个属性注解为ExcelId
        int excelIdCount = 0;
        for (Field field : fieldList) {
            if (field.isAnnotationPresent(ExcelMappedEntity.class)) {
                if (field.isAnnotationPresent(ExcelId.class)) {
                    throw new ExcelCellConfigException("between @ExcelId and @ExcelMappedEntity config at the same property");
                }
                for (Field _field : fieldList) {
                    if (_field.isAnnotationPresent(ExcelId.class)) {
                        excelIdCount++;
                    }
                }
            }
        }
        if (excelIdCount == 0) {
            throw new ExcelCellConfigException(String.format("The class [%s] must config annotation [%s]", rawClass.getName(), ExcelId.class.getName()));
        }
        if (excelIdCount > 1) {
            throw new ExcelCellConfigException(String.format("The one property of class [%s] can config @ExcelId", rawClass.getName()));
        }

        for (Field field : fieldList) {
            if (propertyNames == null || ArrayUtils.containsElement(propertyNames, field.getName())) {
                excelCell = field.getAnnotation(ExcelCell.class);
                if (excelCell == null) continue;

                if (StringUtils.isEmpty(excelCell.value())) {
                    throw new ExcelCellConfigException("ExcelCell.value() value is empty at property [" + field.getType() + "]");
                }
                //title is unique
                for (ExcelCellConfig ecc : parseExcelCellConfigList) {
                    if (ecc.getTitle().equals(excelCell.value())) {
                        throw new ExcelCellConfigException("ExcelCell.value() value[" + excelCell.value() + "] must be unique at class [" + rawClass.getName() + "]");
                    }
                }

                builder = new ExcelCellConfig.Builder()
                        .title(excelCell.value()).type(excelCell.type()).propertyName(field.getName())
                        .excelDateFormat(excelCell.excelDateFormat()).javaDateFormat(excelCell.javaDateFormat());

                ExcelId excelId = field.getAnnotation(ExcelId.class);
                if (excelId != null) {
                    ExcelCellConfig ecc = builder.build();
                    this.excelId_ExcelCellConfig = ecc;
                    parseExcelCellConfigList.add(ecc);
                    parseExcelCellConfigList.add(ecc);
                } else {
                    excelMappedEntity = field.getAnnotation(ExcelMappedEntity.class);
                    if (excelMappedEntity != null) {
                        Class<?> mappedByClass = excelMappedEntity.mappedBy();
                        if (!mappedByClass.isAnnotationPresent(ExcelEntity.class)) {
                            throw new BeanNotConfigAnnotationException(String.format("The class [%s] must config annotation [%s]",
                                    mappedByClass.getName(), ExcelEntity.class.getName()));
                        }
                        builder.child(this.parseExcelCellConfigList(mappedByClass, excelMappedEntity.propertyNames()));
                    }
                    parseExcelCellConfigList.add(builder.build());
                }
            }
        }
        return parseExcelCellConfigList;
    }

    private List<T> parseSheet(Sheet sheet, FormulaEvaluator formulaEvaluator) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        //1.过滤标题等占用rows
        if (this.excelReadConfig.getHeaderRowStart() > 0) {
            for (int i = 0; i < this.excelReadConfig.getHeaderRowStart(); i++) {
                rowIterator.next();
            }
        }

        //2.获取标题头信息
        List<InternalExcelHeader> internalExcelHeaderList = this.getExcelHeaderTitles(rowIterator);
//        Map<String, Integer> headerTitleMap = null;
//        if (this.excelReadConfig.isStrict()) {
//            //严格模式,Bean定义中的列并且是required的需要出现在Excel的header title中
//            for (PropertyCellMapping propertyCellMapping : this.propertyCellMappingList) {
//                if (!headerTitleMap.containsKey(propertyCellMapping.getTitle())) {
//                    throw new ExcelCellConfigException("The excel header cells can not find '" + propertyCellMapping.getTitle() + "'");
//                }
//            }
//        }
        //将title在Excel的位置缓存
        List<ExcelCellConfig> newExcelCellConfigList = new ArrayList<>(this.excelCellConfigs.size());
        for (InternalExcelHeader internalExcelHeader : internalExcelHeaderList) {
            for (ExcelCellConfig excelCellConfig : this.excelCellConfigs) {
                if (internalExcelHeader.title.equals(excelCellConfig.getTitle())) {
//                    internalReadExcelCellConfig = new InternalReadExcelCellConfig();
//                    internalReadExcelCellConfig.cellIndex = internalExcelHeader.cellIndex;
//                    internalReadExcelCellConfig.type = excelCellConfig.getType();
//                    internalReadExcelCellConfig.propertyName = excelCellConfig.getPropertyName();
//                    internalReadExcelCellConfig.excelDateFormat = excelCellConfig.getExcelDateFormat();
//                    internalReadExcelCellConfig.javaDateFormat = excelCellConfig.getJavaDateFormat();

                    if (null != this.excelId_ExcelCellConfig && excelCellConfig.getTitle().equals(this.excelId_ExcelCellConfig.getTitle())) {
//                        this.excelId_ExcelCellConfig.newBuilder().cell
                    }

                    ExcelCellConfig.Builder builder = excelCellConfig.newBuilder().cellIndex(internalExcelHeader.cellIndex);
                    if (CollectionUtils.isNotEmpty(internalExcelHeader.childExcelHeaders) && CollectionUtils.isNotEmpty(excelCellConfig.getExcelCellConfigChildren())) {
                        List<ExcelCellConfig> childList = new ArrayList<>(excelCellConfig.getExcelCellConfigChildren().size());
                        for (InternalExcelHeader childExcelHeader : internalExcelHeader.childExcelHeaders) {
                            for (ExcelCellConfig excelCellConfigChild : excelCellConfig.getExcelCellConfigChildren()) {
                                if (childExcelHeader.title.equals(excelCellConfigChild.getTitle())) {
                                    childList.add(excelCellConfigChild.newBuilder().cellIndex(childExcelHeader.cellIndex).build());
                                    break;
                                }
                            }
                        }
                        builder.clearChild().child(childList);
                    }
                    newExcelCellConfigList.add(builder.build());
                    break;
                }
            }
        }
        this.excelCellConfigs.clear();
        this.excelCellConfigs = newExcelCellConfigList;
        Collections.sort(this.excelCellConfigs);

        List<T> list = new ArrayList<>();
        Row row = null;
        while (rowIterator.hasNext() && (row == null || sheet.getLastRowNum() > row.getRowNum() + this.excelReadConfig.getLastInvalidRow())) {
            row = rowIterator.next();
            DataRowParser<T> dataRowParser = new DataRowParser<>(this);
            for (int i = 0, size = this.excelCellConfigs.size(); i < size; i++) {
                ExcelCellConfig excelCellConfig = this.excelCellConfigs.get(i);
                Cell cell = row.getCell(excelCellConfig.getCellIndex());
                if (i == 0) {
                    CellRangeAddress cellRangeAddress = this.getCellRangeAddress(sheet, cell.getRowIndex(), cell.getColumnIndex());
                    if (cellRangeAddress != null) {

                    }
                }
            }
            for (ExcelCellConfig excelCellConfig : this.excelCellConfigs) {
//                Cell cell = row.getCell(propertyCellMapping.getCellIndex());
                if (CollectionUtils.isNotEmpty(excelCellConfig.getExcelCellConfigChildren())) {

                }
            }
            list.add(dataRowParser.process(row));
        }
        return list;
    }


    private List<InternalExcelHeader> getExcelHeaderTitles(Iterator<Row> rowIterator) {
        List<InternalExcelHeader> internalExcelHeaderList = new ArrayList<>();
        Row row;
        Cell cell;
        Iterator<Cell> cellIterator;

        if (this.excelReadConfig.getHeaderRowUsed() == 1) {
            row = rowIterator.next();
            if (row == null) {
                throw new ExcelReadException("The first header row must not be null.");
            }
            cellIterator = row.cellIterator();
            InternalExcelHeader internalExcelHeader;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                String value = cell.getStringCellValue();
                if (value == null) {
                    logger.debug(" ===> The header cell [{}] is empty,ignore.", new CellReference(row.getRowNum(), cell.getColumnIndex()).formatAsString());
                    continue;
                }
                internalExcelHeader = new InternalExcelHeader();
                internalExcelHeader.title = value;
                internalExcelHeader.cellIndex = cell.getColumnIndex();
                internalExcelHeaderList.add(internalExcelHeader);
            }
            return internalExcelHeaderList;
        }

        row = rowIterator.next();
        if (row == null) {
            throw new ExcelReadException("The first header row must not be null.");
        }

        cellIterator = row.cellIterator();
        InternalExcelHeader internalExcelHeader;
        while (cellIterator.hasNext()) {
            cell = cellIterator.next();
            String value = cell.getStringCellValue();
            if (StringUtils.isEmpty(value)) {
                logger.debug(" ===> The header cell [{}] is empty,ignore.", new CellReference(row.getRowNum(), cell.getColumnIndex()).formatAsString());
                continue;
            }
            internalExcelHeader = new InternalExcelHeader();
            internalExcelHeader.title = value;
            internalExcelHeader.cellIndex = cell.getColumnIndex();
            CellRangeAddress cellRangeAddress = this.getCellRangeAddress(cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex());
            if (cellRangeAddress != null) {
                internalExcelHeader.startRow = cellRangeAddress.getFirstRow();
                internalExcelHeader.endRow = cellRangeAddress.getLastRow();
                internalExcelHeader.startCol = cellRangeAddress.getFirstColumn();
                internalExcelHeader.endCol = cellRangeAddress.getLastColumn();
            }
            internalExcelHeaderList.add(internalExcelHeader);
        }

        List<InternalExcelHeader> last = new ArrayList<>();
        for (int i = 1; i < this.excelReadConfig.getHeaderRowUsed(); i++) {
            row = rowIterator.next();
            cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                String value = cell.getStringCellValue();
                if (StringUtils.isEmpty(value)) {
                    logger.debug(" ===> The header cell [{}] is empty,ignore.", new CellReference(row.getRowNum(), cell.getColumnIndex()).formatAsString());
                    continue;
                }
                internalExcelHeader = new InternalExcelHeader();
                internalExcelHeader.title = value;
                internalExcelHeader.cellIndex = cell.getColumnIndex();
                internalExcelHeader.rowIndex = cell.getRowIndex();

                last.add(internalExcelHeader);
            }
        }

        for (InternalExcelHeader excelHeader : internalExcelHeaderList) {
            List<InternalExcelHeader> children = new ArrayList<>();
            for (InternalExcelHeader header : last) {
                if (excelHeader.inCol(header.cellIndex)) {
                    children.add(header);
                }
            }
            excelHeader.childExcelHeaders = children;
        }
        return internalExcelHeaderList;
    }


    /**
     * 解析JavaBean中的带有{@code ExcelCell}属性
     */
    private void parseBeanPropertyCellDescriptor() {
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(this.excelReadConfig.getRawClass());
        fieldList = ReflectionUtils.filter(fieldList, new StaticFieldFilter());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                PropertyCellMapping propertyCellMapping = new PropertyCellMapping();
                propertyCellMapping.setTitle(excelCell.value());
                propertyCellMapping.setRequired(excelCell.required());
                propertyCellMapping.setPropertyName(fieldName);
                propertyCellMapping.setType(excelCell.type());
                propertyCellMapping.setExcelDateFormat(excelCell.excelDateFormat());
//                propertyCellMappingList.add(propertyCellMapping);
            }
        }
    }

    private CellRangeAddress getCellRangeAddress(Sheet sheet, int rowIndex, int colIndex) {
        for (CellRangeAddress cellRangeAddress : sheet.getMergedRegions()) {
            if (rowIndex >= cellRangeAddress.getFirstRow() && rowIndex <= cellRangeAddress.getLastRow() &&
                    colIndex >= cellRangeAddress.getFirstColumn() && colIndex <= cellRangeAddress.getLastColumn()) {
                return cellRangeAddress;
            }
        }
        return null;
    }
}
