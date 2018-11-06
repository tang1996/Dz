package com.dz.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {

	// 业务员月绩效统计
	public static String[] salerDetailed(String[][] datas, String Path, String city) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("业务员月绩效统计");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("姓名");
		cell = row.createCell(1);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("联系方式");
		cell = row.createCell(2);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("个人主营绩效");
		cell = row.createCell(3);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("个人副营绩效");
		cell = row.createCell(4);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("团队主营绩效");
		cell = row.createCell(5);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("团队副营绩效");
		cell = row.createCell(6);
		sheet.setColumnWidth(cell.getColumnIndex(), 50 * 50);
		cell.setCellValue("绩效等级");
		cell = row.createCell(7);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("银行卡号");
		cell = row.createCell(8);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("身份证号");
		cell = row.createCell(9);
		sheet.setColumnWidth(cell.getColumnIndex(), 80 * 50);
		cell.setCellValue("银行类型");
		cell = row.createCell(10);
		sheet.setColumnWidth(cell.getColumnIndex(), 120 * 50);
		cell.setCellValue("开户行名称");
		cell = row.createCell(11);
		sheet.setColumnWidth(cell.getColumnIndex(), 55 * 50);
		cell.setCellValue("职务");

		for (int i = 0; i < datas.length; i++) {
			HSSFRow content = sheet.createRow(i + 1);
			content.createCell(0).setCellValue(datas[i][0]);
			content.createCell(1).setCellValue(datas[i][1]);
			content.createCell(2).setCellValue(datas[i][2]);
			content.createCell(3).setCellValue(datas[i][3]);
			content.createCell(4).setCellValue(datas[i][4]);
			content.createCell(5).setCellValue(datas[i][5]);
			content.createCell(6).setCellValue(datas[i][6]);
			content.createCell(7).setCellValue(datas[i][7]);
			content.createCell(8).setCellValue(datas[i][8]);
			content.createCell(9).setCellValue(datas[i][9]);
			content.createCell(10).setCellValue(datas[i][10]);
			content.createCell(11).setCellValue(datas[i][11]);
		}
		String[] arr = new String[] { "false", "导出失败,请稍后重试", ""};
		try {
			String contextPath = Path;	//ynw
			String filename = "";	//ynw
			
			//FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\" + city + "市：业务员月绩效统计" + ".xls");
			FileOutputStream fos = new FileOutputStream( contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" );	//ynw
			filename = contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" ;	//ynw
			
			workbook.write(fos);
			System.out.println("写入成功(业务员月绩效统计)"+ contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls");
			fos.close();
//			workbook.close();
			arr[0] = "true";
			arr[1] = "导出成功,文件位于"+contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls";
			
			arr[2] = filename;	//ynw
		} catch (IOException e) {
			arr[1] = "导出失败," + e;
			e.printStackTrace();
		}
		return arr;
	}

	// 骑手月绩效统计
	public static String[] riderDetailed(String[][] datas, String Path, String city) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("骑手月绩效统计");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("姓名");
		cell = row.createCell(1);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("联系方式");
		cell = row.createCell(2);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("订单量");
		cell = row.createCell(3);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("月收益");
		cell = row.createCell(4);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("综合评分");
		cell = row.createCell(5);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("银行卡号");
		cell = row.createCell(6);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("身份证号");
		cell = row.createCell(7);
		sheet.setColumnWidth(cell.getColumnIndex(), 80 * 50);
		cell.setCellValue("银行类型");
		cell = row.createCell(8);
		sheet.setColumnWidth(cell.getColumnIndex(), 120 * 50);
		cell.setCellValue("开户行名称");

		for (int i = 0; i < datas.length; i++) {
			HSSFRow content = sheet.createRow(i + 1);
			content.createCell(0).setCellValue(datas[i][0]);
			content.createCell(1).setCellValue(datas[i][1]);
			content.createCell(2).setCellValue(datas[i][2]);
			content.createCell(3).setCellValue(datas[i][3]);
			content.createCell(4).setCellValue(datas[i][4]);
			content.createCell(5).setCellValue(datas[i][5]);
			content.createCell(6).setCellValue(datas[i][6]);
			content.createCell(7).setCellValue(datas[i][7]);
			content.createCell(8).setCellValue(datas[i][8]);
		}
		String[] arr = new String[] { "false", "导出失败,请稍后重试" , ""};
		try {		
			String contextPath = Path;	//ynw
			String filename = "";	//ynw
			
			//FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\" + city + "市：骑手月绩效统计" + ".xls");
			FileOutputStream fos = new FileOutputStream( contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" );	//ynw
			filename = contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" ;	//ynw
			
			workbook.write(fos);
			System.out.println("写入成功(骑手月绩效统计)" + contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls");
			fos.close();
			arr[0] = "true";
			arr[1] = "导出成功,文件位于"+contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls";
			
			arr[2] = filename;	//ynw
//			workbook.close();
		} catch (IOException e) {
			arr[1] = "导出失败," + e;
			e.printStackTrace();
		}
		return arr;
	}
	
	// 商家缴费明细统计	ynw
	public static String[] companyDetailed(String[][] datas, String Path, String city) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("商家缴费明细统计");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("商家名称");
		cell = row.createCell(1);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("商家联系方式");
		cell = row.createCell(2);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("业务员名称");
		cell = row.createCell(3);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("业务员联系方式");
		cell = row.createCell(4);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("地区");
		cell = row.createCell(5);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("到期时间");
		cell = row.createCell(6);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("剩余时间");
		cell = row.createCell(7);
		sheet.setColumnWidth(cell.getColumnIndex(), 80 * 50);
		cell.setCellValue("商家地址");
		cell = row.createCell(8);
		sheet.setColumnWidth(cell.getColumnIndex(), 120 * 50);
		cell.setCellValue("年费费用");

		for (int i = 0; i < datas.length; i++) {
			HSSFRow content = sheet.createRow(i + 1);
			content.createCell(0).setCellValue(datas[i][0]);
			content.createCell(1).setCellValue(datas[i][1]);
			content.createCell(2).setCellValue(datas[i][2]);
			content.createCell(3).setCellValue(datas[i][3]);
			content.createCell(4).setCellValue(datas[i][4]);
			content.createCell(5).setCellValue(datas[i][5]);
			content.createCell(6).setCellValue(datas[i][6]);
			content.createCell(7).setCellValue(datas[i][7]);
			content.createCell(8).setCellValue(datas[i][8]);
		}
		String[] arr = new String[] { "false", "导出失败,请稍后重试", "" };
		try {
			String contextPath = Path;	//ynw
			String filename = "";	//ynw
			
			FileOutputStream fos = new FileOutputStream( contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" );	//ynw
			filename = contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" ;	//ynw
			
			workbook.write(fos);
			System.out.println("写入成功(商家缴费明细统计)" + contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls");
			fos.close();
			arr[0] = "true";
			arr[1] = "导出成功,文件位于"+contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls";
			
			arr[2] = filename;	//ynw
		} catch (IOException e) {
			arr[1] = "导出失败," + e;
			e.printStackTrace();
		}
		return arr;
	}
	
	// 商家缴费记录	ynw
	public static String[] companyPayDetailed(String[][] datas, String Path, String CompanyName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("商家缴费记录");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("商家名称");
		cell = row.createCell(1);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("订单号");
		cell = row.createCell(2);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("支付金额");
		cell = row.createCell(3);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("支付时间");
		cell = row.createCell(4);
		sheet.setColumnWidth(cell.getColumnIndex(), 65 * 50);
		cell.setCellValue("续费时长");
		cell = row.createCell(5);
		sheet.setColumnWidth(cell.getColumnIndex(), 110 * 50);
		cell.setCellValue("是否到期");

		for (int i = 0; i < datas.length; i++) {
			HSSFRow content = sheet.createRow(i + 1);
			content.createCell(0).setCellValue(datas[i][0]);
			content.createCell(1).setCellValue(datas[i][1]);
			content.createCell(2).setCellValue(datas[i][2]);
			content.createCell(3).setCellValue(datas[i][3]);
			content.createCell(4).setCellValue(datas[i][4]);
			content.createCell(5).setCellValue(datas[i][5]);
		}
		String[] arr = new String[] { "false", "导出失败,请稍后重试", ""};
		try {
			String contextPath = Path;
			String filename = "";
			
			FileOutputStream fos = new FileOutputStream( contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" );
			filename = contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls" ;
			
			workbook.write(fos);
			System.out.println("写入成功(商家缴费记录)" + contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls");
			fos.close();
			arr[0] = "true";
			arr[1] = "导出成功,文件位于"+contextPath +"\\DzPlfrom\\"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ ".xls";
			
			arr[2] = filename;
		} catch (IOException e) {
			arr[1] = "导出失败," + e;
			e.printStackTrace();
		}
		return arr;
	}
	
	
}
