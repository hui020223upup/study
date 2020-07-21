package com.study.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: whh
 * @date: 2020.07.21
 **/
public class ExcelUtil {

    public static List<List<String>> readXlsx(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List<String>> result = new ArrayList<>();
        //循环每一页，并处理当前循环页
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            //处理当前页，循环读取每一行
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                //遍历该行获取处理每个cell元素
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                result.add(rowList);
            }
        }
        return result;

    }

    public static String getStringVal(XSSFCell cell) {
        switch (cell.getCellType()) {
            case _NONE:
                return "";
            case NUMERIC:
                DecimalFormat df = new DecimalFormat("0");
                return df.format(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case ERROR:
                return "";
            default:
                return "";
        }
    }

}