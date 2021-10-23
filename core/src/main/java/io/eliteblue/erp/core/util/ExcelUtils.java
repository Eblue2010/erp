package io.eliteblue.erp.core.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelUtils {

    private static final String propDir = System.getProperty("user.dir");
    private static final String filepath = propDir+"/data/empmast-1.xlsx";
    private static final String sheetName = "empmast";
    private static final DataFormatter format = new DataFormatter();

    public static Workbook workbook;
    public static Sheet worksheet;

    public static void initializeWorkbook() throws Exception {
        //InputStream file = new FileInputStream(filepath);
        if(workbook == null && worksheet == null) {
            workbook = new XSSFWorkbook(filepath);
            worksheet = workbook.getSheet(sheetName);
        }
    }

    public static void initializeWithFilename(String fileName, String sname) throws Exception {
        String path = propDir+"/data/"+fileName;
        File file = new File(path);
        //if(workbook == null && worksheet == null) {
            workbook = WorkbookFactory.create(new FileInputStream(file));
            worksheet = workbook.getSheet(sname);
        //}
    }

    public static void initializeWithInputStream(InputStream file, String sname) throws Exception {
        workbook = WorkbookFactory.create(file);
        worksheet = workbook.getSheet(sname);
    }

    public static Object getCellData(int row, int cell) throws Exception {
        initializeWorkbook();
        Object value = format.formatCellValue(worksheet.getRow(row).getCell(cell));
        //System.out.println("DATA CELL VALUE: "+value);
        return value;
    }

    public static void setCell(int row, int cell, Object val, CellStyle... style) throws Exception{
        initializeWorkbook();
        //System.out.println("ROW: "+row+" CELL: "+cell);
        Row _row = worksheet.getRow(row);
        Cell _cell;
        if(_row == null) {
            _row = worksheet.createRow(row);
            _cell = _row.createCell(cell);
        }
        else {
            _cell = _row.getCell(cell);
            if(_cell == null) {
                _cell = _row.createCell(cell);
            }
        }
        if(style != null && style.length > 0)
            _cell.setCellStyle(style[0]);
        if (val instanceof String)
            _cell.setCellValue((String) val);
        if (val instanceof Long)
            _cell.setCellValue((Long) val);
        if (val instanceof Integer)
            _cell.setCellValue((Integer) val);
    }

    public static void evaluateCell(int row, int cell) throws Exception {
        initializeWorkbook();
        Cell _cell = worksheet.getRow(row).getCell(cell);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateFormulaCell(_cell);
    }

    public static CellValue evaluateCellFormula(int row, int cell) throws Exception {
        initializeWorkbook();
        Cell _cell = worksheet.getRow(row).getCell(cell);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return evaluator.evaluate(_cell);
    }

    public static CellStyle getCellStyle(int row, int cell) throws Exception {
        initializeWorkbook();
        Cell _cell = worksheet.getRow(row).getCell(cell);
        return _cell.getCellStyle();
    }

    public static void evaluateAllCells() throws Exception {
        initializeWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();
    }

    public static Integer getRowCount() throws Exception {
        initializeWorkbook();
        Integer rowCount = worksheet.getPhysicalNumberOfRows();
        System.out.println("ROW COUNT: "+rowCount.toString());
        return rowCount;
    }
}
