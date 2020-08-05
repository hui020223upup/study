package com.study.util;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 导出excel文件（可导出图片）
 *
 * @author: whh
 * @date: 2019.10.15
 **/
public class ExportExcelV2Util {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelV2Util.class);


    private ExportExcelV2Util() {

    }

    public static void exportWithPictureExcel(final HttpServletResponse response, List<String[]> list, String fileName) {
        long startTime = System.currentTimeMillis();
        if (!fileName.endsWith(".xls")) {
            LOGGER.error("exportExcel fileName={} style is no xlsx", fileName);
            return;
        }
        ServletOutputStream out = null;
        HSSFWorkbook wb = null;
        try {
            //准备导出
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            out = response.getOutputStream();
            // 声明一个工作薄
            wb = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sh = wb.createSheet();
            //图片byte数组流
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            // 写入excel内容
            for (int rowNum = 0; rowNum < list.size(); rowNum++) {
                Row row = sh.createRow(rowNum);
                for (int cellNum = 0; cellNum < list.get(0).length; cellNum++) {
                    Cell cell = row.createCell(cellNum);
                    String data = list.get(rowNum)[cellNum];

                    //获取图片URL
                    String suffix = data.substring(data.lastIndexOf(".") + 1);
                    if (isPicType(suffix)) {
                        //获取图片类型
                        int pictureType = changePicType(suffix);

                        URL url = new URL(data);
                        BufferedImage bufferImg = ImageIO.read(url);
                        sh.setColumnWidth(cellNum, 30 * 256);
                        //原始宽度
                        int width = bufferImg.getWidth();
                        //原始高度
                        int height = bufferImg.getHeight();
                        // 一个12号字体的宽度为13,前面已设置了列的宽度为30*256，故这里的等比例高度计算如下
                        height = (int) Math.round((height * (30 * 13) * 1.0 / width));
                        // excel单元格高度是以点单位，1点=2像素; POI中Height的单位是1/20个点，故设置单元的等比例高度如下
                        row.setHeight((short) (height / 2 * 20));

                        ImageIO.write(bufferImg, suffix, byteArrayOut);
                        HSSFPatriarch hssfShapes = sh.createDrawingPatriarch();
                        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
                        //anchor主要用于设置图片的属性
                        HSSFClientAnchor anchor =
                                new HSSFClientAnchor(0, //x缩放
                                        0, // y缩放
                                        1023, //最大1023
                                        255, //最大255
                                        (short) cellNum,
                                        rowNum,
                                        (short) cellNum,
                                        rowNum
                                );
                        //插入图片
                        hssfShapes.createPicture(anchor,
                                wb.addPicture(byteArrayOut.toByteArray(),
                                        pictureType));
                    } else {
                        cell.setCellValue(data);
                    }
                }
            }
            //关闭流
            if (byteArrayOut != null) {
                byteArrayOut.close();
            }
            wb.write(out);
        } catch (IOException e) {
            LOGGER.error("exportExcel error", e);
        } catch (Exception e) {
            LOGGER.error("exportExcel error", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("out close error", e);
                }
            }
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    LOGGER.error("wb close error", e);
                }
            }
            long end = System.currentTimeMillis();
            long cost = end - startTime;
            LOGGER.info("exportExcel fileName={} cost={} ms", fileName, cost);
        }
    }

    /**
     * 改变图片类型
     *
     * @param suffix
     * @return
     */
    private static boolean isPicType(String suffix) {

        if (suffix.toUpperCase().equals("PNG")) {
            return true;
        }
        if (suffix.toUpperCase().equals("TIFF")) {
            return true;
        }
        if (suffix.toUpperCase().equals("JPEG")) {
            return true;
        }
        if (suffix.toUpperCase().equals("WPG")) {
            return true;
        }
        if (suffix.toUpperCase().equals("PNG")) {
            return true;
        }
        if (suffix.toUpperCase().equals("PICT")) {
            return true;
        }
        if (suffix.toUpperCase().equals("GIF")) {
            return true;
        }
        return false;
    }


    /**
     * 改变图片类型
     *
     * @param suffix
     * @return
     */
    private static int changePicType(String suffix) {
        int pictureType = 5; //默认5 JPEG格式

        if (suffix.toUpperCase().equals("PNG")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_JPEG;
        }
        if (suffix.toUpperCase().equals("TIFF")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_TIFF;
        }
        if (suffix.toUpperCase().equals("JPEG")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_JPEG;
        }
        if (suffix.toUpperCase().equals("WPG")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_WPG;
        }
        if (suffix.toUpperCase().equals("PNG")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_PNG;
        }
        if (suffix.toUpperCase().equals("PICT")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_PICT;
        }
        if (suffix.toUpperCase().equals("GIF")) {
            pictureType = XSSFWorkbook.PICTURE_TYPE_GIF;
        }
        return pictureType;
    }

}
