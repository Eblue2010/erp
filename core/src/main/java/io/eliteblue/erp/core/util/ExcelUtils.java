package io.eliteblue.erp.core.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelUtils {

    private static final String propDir = System.getProperty("user.dir");
    private static final String filepath = propDir+"/data/empmast-1.xlsx";
    private static final String sheetName = "empmast";
    private static final DataFormatter format = new DataFormatter();

    public static XSSFWorkbook workbook;
    public static XSSFSheet worksheet;

    public static void initializeWorkbook() throws Exception {
        //InputStream file = new FileInputStream(filepath);
        if(workbook == null && worksheet == null) {
            workbook = new XSSFWorkbook(filepath);
            worksheet = workbook.getSheet(sheetName);
        }
    }

    public static Object getCellData(int row, int cell) throws Exception {
        initializeWorkbook();
        Object value = format.formatCellValue(worksheet.getRow(row).getCell(cell));
        //System.out.println("DATA CELL VALUE: "+value);
        return value;
    }

    public static Integer getRowCount() throws Exception {
        initializeWorkbook();
        Integer rowCount = worksheet.getPhysicalNumberOfRows();
        System.out.println("ROW COUNT: "+rowCount.toString());
        return rowCount;
    }
}
