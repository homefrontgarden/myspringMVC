package com.jsalpha.utils.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * workbook工具类
 * @author dengjingsi
 */
public class WorkBookUtil {
    /**
     * 保存workbook到指定路径
     * @param workbook
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String saveExcel(Workbook workbook, String filePath, String fileName) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath + fileName);
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        }
        return filePath+fileName;
    }

    /**
     * 支持多种格式value的统一操作（尽量不要改此方法的使用逻辑，但是可以添加更多格式value的支持）
     * @param cell
     * @param object
     */
    public static void setIfCellValue(Cell cell, Object object){
        if(object instanceof String){
            cell.setCellValue((String) object);
        }else if(object instanceof Integer){
            cell.setCellValue((Integer) object);
        }else if(object instanceof Long){
            cell.setCellValue((Integer) object);
        }else if(object instanceof Float){
            cell.setCellValue((Float) object);
        }else if (object instanceof Double) {
            cell.setCellValue((Double) object);
        }else if (object instanceof Boolean) {
            cell.setCellValue((Boolean) object);
        }else if(object instanceof Date){
            cell.setCellValue((Date) object);
        }else if(object instanceof Calendar){
            cell.setCellValue((Calendar) object);
        }else if(object instanceof XSSFRichTextString){
            cell.setCellValue((XSSFRichTextString) object);
        }
    }

    /**
     * 支持多种格式value的统一操作（尽量不要改此方法的使用逻辑，但是可以添加更多格式value的支持）
     * @param cell
     * @param object
     */
    public static void setCellValue(Cell cell, Object object){
        if(object == null){
            return;
        }
        switch(object.getClass().getName()){
            case "java.lang.Integer":
                cell.setCellValue((Integer) object);
                break;
            case "java.lang.Long":
                cell.setCellValue((Long) object);
                break;
            case "java.lang.Float":
                cell.setCellValue((Float) object);
                break;
            case "java.lang.Double":
                cell.setCellValue((Double) object);
                break;
            case "java.lang.Boolean":
                cell.setCellValue((Boolean) object);
                break;

            case "java.util.Date":
                cell.setCellValue((Date) object);
                break;

            case "java.util.GregorianCalendar":
                cell.setCellValue((Calendar) object);
                break;
            case "org.apache.poi.xssf.usermodel.XSSFRichTextString":
                cell.setCellValue((XSSFRichTextString) object);
                break;
            default:
                cell.setCellValue((String) object);
        }
    }

    /**
     * 在row中指定列创建cell，并赋值value
     * @param row
     * @param index
     * @param value
     */
    public static Cell createCellAndSetValue(Row row, int index, Object value) {
        Cell cell = row.createCell(index);
        setCellValue(cell, value);
        return cell;
    }
    /**
     * 根据sheetMergedRegion提供的坐标，合并sheet
     * @param sheetMergedRegion 二维数组，每个二维数据中的一维数组必须长度为4
     * @param sheet
     */
    public static void addMergedRegion(int[][] sheetMergedRegion,Sheet sheet){
        for(int index = 0,length = sheetMergedRegion.length; index < length; index++){
            CellRangeAddress region = new CellRangeAddress(sheetMergedRegion[index][0], sheetMergedRegion[index][1], sheetMergedRegion[index][2], sheetMergedRegion[index][3]);
            sheet.addMergedRegion(region);
        }
    }
}

