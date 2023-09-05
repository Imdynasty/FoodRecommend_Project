package com.foocmend.commons.restaurantsdata;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ExcelUpload {
    public static void main(String[] args) {
        excelToDb();

    }

    public static void excelToDb() {
        String url = "jdbc:mysql://localhost:3306/foocmend";
        String username = "foocmend";
        String password = "_aA123456";
        String sql = "INSERT INTO RESTAURANTS (zipcode, address, roadAddress, zonecode, storeName, type, xpos, ypos, wishCnt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream("서울_인천.xlsx"));
             OPCPackage opcPackage = OPCPackage.open(bis)) {
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            root : for(int k = 0 ; k < workbook.getNumberOfSheets(); k++) {
                XSSFSheet sheet = workbook.getSheetAt(k);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    try {
                        XSSFRow row = sheet.getRow(i);

                        XSSFCell[] cells = new XSSFCell[8];

                        cells[0] = row.getCell(0); // 우편번호
                        cells[1] = row.getCell(1); // 소재지 전체 주소
                        cells[2] = row.getCell(2); // 도로명 전체 주소
                        cells[3] = row.getCell(3); // 도로명 우편번호
                        cells[4] = row.getCell(4); // 사업장명

                        cells[5] = row.getCell(5); // 업태구분명
                        if(getString(cells[5]).equals("호프/통닭")
                                || getString(cells[5]).equals("정종/대포집/소주방") || getString(cells[5]).equals("감성주점") ){cells[5].setCellValue("술집");}

                        if(getString(cells[5]).equals("패밀리레스토랑")
                                || getString(cells[5]).equals("외국음식전문점(인도,태국등)") || getString(cells[5]).equals("경양식")) {cells[5].setCellValue("양식");}

                        if(getString(cells[5]).equals("까페")
                                || getString(cells[5]).equals("키즈카페") || getString(cells[5]).equals("커피숍") || getString(cells[5]).equals("전통찻집") || getString(cells[5]).equals("라이브카페")) {cells[5].setCellValue("카페");}

                        if(getString(cells[5]).equals("통닭(치킨)")
                                || getString(cells[5]).equals("탕류(보신용)") || getString(cells[5]).equals("출장조리") || getString(cells[5]).equals("이동조리") || getString(cells[5]).equals("식육(숯불구이)") || getString(cells[5]).equals("복어취급") || getString(cells[5]).equals("냉면집") || getString(cells[5]).equals("김밥(도시락)")) {cells[5].setCellValue("한식");}

                        if(getString(cells[5]).equals("중국식")) {cells[5].setCellValue("중식");}
                        if(getString(cells[5]).equals("뷔페식")) {cells[5].setCellValue("뷔페");}
                        if(getString(cells[5]).isBlank()) {cells[5].setCellValue("기타");}

                        cells[6] = row.getCell(6); // 좌표(x)
                        cells[7] = row.getCell(7); // 좌표(y)
                        if (getString(cells[0]).isBlank() || getString(cells[1]).isBlank())
                        {
                            break root;
                        }
                        try {
                            for (int j = 1; j <= cells.length; j++) {
                                 pstmt.setString(j, getString(cells[j-1]));
                            }
                            System.out.println("--------------------------------------------------");
                            System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s %n", getString(cells[0]), getString(cells[1]), getString(cells[2]), getString(cells[3]), getString(cells[4]), getString(cells[5]), getString(cells[6]), getString(cells[7]));

                            System.out.println("---------------------------------------------------");
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                            break root;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getString(XSSFCell cell) {
        if (cell == null) return "";
        return cell.getStringCellValue().trim();
    }



}