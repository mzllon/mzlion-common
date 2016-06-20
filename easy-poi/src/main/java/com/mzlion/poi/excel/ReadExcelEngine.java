package com.mzlion.poi.excel;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelId;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.ReadExcelConfig;
import com.mzlion.poi.exception.ExcelCellHeaderTitleException;
import com.mzlion.poi.exception.ReadExcelException;
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
 * Created by mzlion on 2016/6/16.
 */
class ReadExcelEngine<E> {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(ReadExcelEngine.class);
    //导入的配置选项
    final ReadExcelConfig readExcelConfig;

    private List<ReadExcelCellHeaderConfig> readExcelCellHeaderConfigList;
    FormulaEvaluator formulaEvaluator;

    ReadExcelEngine(ReadExcelConfig readExcelConfig) {
        this.readExcelConfig = readExcelConfig;
        if (ClassUtils.isAssignable(Map.class, this.readExcelConfig.getRawClass())) {
            this.readExcelCellHeaderConfigList = this.convertByMapBean();
        } else {
            this.readExcelCellHeaderConfigList = this.convertByJavaBean(this.readExcelConfig.getRawClass(), null);
        }
    }

    List<E> read(InputStream in) {
        logger.debug(" ===> Starting read and parse excel");
        //记录解析开始时间
        long startTime = System.currentTimeMillis();
        Workbook workbook = null;
        try {
            if (!in.markSupported()) in = new PushbackInputStream(in, 8);
            if (POIFSFileSystem.hasPOIFSHeader(in)) {
//                in = ExcelType.XLS;
                workbook = new HSSFWorkbook(in);
            } else if (POIXMLDocument.hasOOXMLHeader(in)) {
                workbook = new XSSFWorkbook(OPCPackage.open(in));
            } else {
                throw new ReadExcelException("InputStream was not MS ExcelEntity stream.");
            }
            long readExcelTime = System.currentTimeMillis();
            logger.debug(" ===> Reading excel cost {} milliseconds.", (readExcelTime - startTime));

            //获取公式解析器
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            List<E> dataSet = new ArrayList<>();

            //按照sheet读取
            for (int i = this.readExcelConfig.getSheetIndex() - 1; i < this.readExcelConfig.getSheetNum(); i++) {
                dataSet.addAll(this.parseSheet(workbook.getSheetAt(i)));
            }
            return dataSet;
        } catch (IOException | InvalidFormatException e) {
            throw new ReadExcelException("Reading excel failed.", e);
        } finally {
            PoiUtils.closeQuitely(workbook);
        }
    }

    private List<ReadExcelCellHeaderConfig> convertByMapBean() {
        List<ReadExcelCellHeaderConfig> configs = new ArrayList<>(this.readExcelConfig.getCellHeaderConfigList().size());
        for (ExcelCellHeaderConfig excelCellHeaderConfig : this.readExcelConfig.getCellHeaderConfigList()) {
            configs.add(new ReadExcelCellHeaderConfig(excelCellHeaderConfig));
        }
        return configs;
    }

    private List<ReadExcelCellHeaderConfig> convertByJavaBean(Class<?> beanClass, String[] includePropertyNames) {
        List<ReadExcelCellHeaderConfig> readExcelCellHeaderConfigList = new ArrayList<>();
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(beanClass);

        for (Field field : fieldList) {
            if (includePropertyNames != null && !ArrayUtils.containsElement(includePropertyNames, field.getName())) {
                continue;
            }

            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell == null) continue;

            ReadExcelCellHeaderConfig readExcelCellHeaderConfig = new ReadExcelCellHeaderConfig();
            readExcelCellHeaderConfig.title = excelCell.value();
            readExcelCellHeaderConfig.excelCellType = excelCell.excelCellType();
            readExcelCellHeaderConfig.excelDateFormat = excelCell.excelDateFormat();
            readExcelCellHeaderConfig.javaDateFormat = excelCell.javaDateFormat();
            readExcelCellHeaderConfig.propertyName = field.getName();
            readExcelCellHeaderConfig.isExcelId = field.isAnnotationPresent(ExcelId.class);
            ExcelMappedEntity excelMappedEntity = field.getAnnotation(ExcelMappedEntity.class);
            if (excelMappedEntity != null) {
                Class<?> targetClass = excelMappedEntity.targetClass();
                readExcelCellHeaderConfig.children = this.convertByJavaBean(targetClass, excelMappedEntity.propertyNames());
                readExcelCellHeaderConfig.targetClass = targetClass;
            }
            readExcelCellHeaderConfigList.add(readExcelCellHeaderConfig);
        }
        return readExcelCellHeaderConfigList;
    }


    private List<E> parseSheet(Sheet sheet) {
        logger.debug(" ===> Reading sheet is {}", sheet.getSheetName());
        Iterator<Row> rowIterator = sheet.rowIterator();
        //1.过滤标题等占用rows
        if (this.readExcelConfig.getHeaderRowStart() > 0) {
            for (int i = 0; i < this.readExcelConfig.getHeaderRowStart() - 1; i++) {
                rowIterator.next();
            }
        }

        //2.获取标题头信息
        List<InternalExcelHeader> internalExcelHeaderList = this.getExcelHeaders(rowIterator);

        List<ReadExcelCellHeaderConfig> headerConfigList;
        if (ClassUtils.isAssignable(Map.class, this.readExcelConfig.getRawClass())) {
            headerConfigList = this.mergeCellHeadersByMap(internalExcelHeaderList);
        } else {
            //3.将Excel解析的InternalExcelHeader和JavaBean解析的ReadExcelCellHeaderConfig合并起来
            headerConfigList = this.mergeCellHeaders(internalExcelHeaderList);
            Collections.sort(headerConfigList);
            this.readExcelCellHeaderConfigList = null;
        }

        DataRowReader<E> dataRowReader = new DataRowReader<>(headerConfigList, this);
        return dataRowReader.read(rowIterator, sheet);
    }

    private List<ReadExcelCellHeaderConfig> mergeCellHeadersByMap(List<InternalExcelHeader> internalExcelHeaderList) {
        List<ReadExcelCellHeaderConfig> headerConfigList = new ArrayList<>(internalExcelHeaderList.size());
        for (InternalExcelHeader internalExcelHeader : internalExcelHeaderList) {
            ReadExcelCellHeaderConfig _config = this.getByTitle(internalExcelHeader.title);
            if (_config == null) {
                continue;
            }
            ReadExcelCellHeaderConfig readExcelCellHeaderConfig = new ReadExcelCellHeaderConfig(_config);
            readExcelCellHeaderConfig.cellIndex = internalExcelHeader.cellIndex;
            headerConfigList.add(readExcelCellHeaderConfig);
        }
        return headerConfigList;
    }

    private List<ReadExcelCellHeaderConfig> mergeCellHeaders(List<InternalExcelHeader> internalExcelHeaderList) {
        List<ReadExcelCellHeaderConfig> headerConfigList = new ArrayList<>(internalExcelHeaderList.size());
        for (InternalExcelHeader internalExcelHeader : internalExcelHeaderList) {
            ReadExcelCellHeaderConfig _config = this.getByTitle(internalExcelHeader.title);
            if (_config == null) {
                continue;
            }
            ReadExcelCellHeaderConfig readExcelCellHeaderConfig = new ReadExcelCellHeaderConfig(_config);
            readExcelCellHeaderConfig.cellIndex = internalExcelHeader.cellIndex;
            List<InternalExcelHeader> childExcelHeaders = internalExcelHeader.childExcelHeaders;
            if (CollectionUtils.isNotEmpty(childExcelHeaders)) {
                List<ReadExcelCellHeaderConfig> _children = readExcelCellHeaderConfig.children;
                if (CollectionUtils.isEmpty(_children)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("The titles [");
                    for (InternalExcelHeader childExcelHeader : childExcelHeaders) {
                        sb.append(childExcelHeader.title).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append("] ");
                    sb.append(" must config by @ExcelMappedEntity.");
                    throw new ExcelCellHeaderTitleException(sb.toString());
                }
                List<ReadExcelCellHeaderConfig> children = new ArrayList<>(_children.size());
                for (InternalExcelHeader childExcelHeader : childExcelHeaders) {
                    ReadExcelCellHeaderConfig _child = this.getByTitle(childExcelHeader.title, readExcelCellHeaderConfig);
                    if (_child == null) {
                        throw new ExcelCellHeaderTitleException("Title [" + internalExcelHeader.title + "] @ExcelCell can not find.");
                    }
                    if (childExcelHeader.title.equals(_child.title)) {
                        ReadExcelCellHeaderConfig child = new ReadExcelCellHeaderConfig(_child);
                        child.cellIndex = childExcelHeader.cellIndex;
                        children.add(child);
                    }
                }
                readExcelCellHeaderConfig.children = children;
            }
            headerConfigList.add(readExcelCellHeaderConfig);
        }
        return headerConfigList;
    }

    private ReadExcelCellHeaderConfig getByTitle(String title, ReadExcelCellHeaderConfig parent) {
        for (ReadExcelCellHeaderConfig readExcelCellHeaderConfig : parent.children) {
            if (title.equals(readExcelCellHeaderConfig.title)) return readExcelCellHeaderConfig;
        }
        return null;
    }

    private ReadExcelCellHeaderConfig getByTitle(String title) {
        for (ReadExcelCellHeaderConfig readExcelCellHeaderConfig : this.readExcelCellHeaderConfigList) {
            if (readExcelCellHeaderConfig.title.equals(title)) return readExcelCellHeaderConfig;
        }
        return null;
    }

    private List<InternalExcelHeader> getExcelHeaders(Iterator<Row> rowIterator) {
        List<InternalExcelHeader> internalExcelHeaderList = new ArrayList<>();
        Row row;
        Cell cell;
        Iterator<Cell> cellIterator;

        if (this.readExcelConfig.getHeaderRowUsed() == 1) {
            row = rowIterator.next();
            if (row == null) {
                throw new ReadExcelException("The first header row must not be null.");
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
                internalExcelHeaderList.add(internalExcelHeader);
            }
            return internalExcelHeaderList;
        }

        row = rowIterator.next();
        if (row == null) {
            throw new ReadExcelException("The first header row must not be null.");
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
        for (int i = 1; i < this.readExcelConfig.getHeaderRowUsed(); i++) {
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
            if (CollectionUtils.isNotEmpty(children)) excelHeader.childExcelHeaders = children;
        }
        return internalExcelHeaderList;
    }

    CellRangeAddress getCellRangeAddress(Sheet sheet, int rowIndex, int colIndex) {
        for (CellRangeAddress cellRangeAddress : sheet.getMergedRegions()) {
            if (rowIndex >= cellRangeAddress.getFirstRow() && rowIndex <= cellRangeAddress.getLastRow() &&
                    colIndex >= cellRangeAddress.getFirstColumn() && colIndex <= cellRangeAddress.getLastColumn()) {
                return cellRangeAddress;
            }
        }
        return null;
    }

}
