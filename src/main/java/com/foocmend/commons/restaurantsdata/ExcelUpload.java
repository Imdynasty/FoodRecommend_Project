package com.foocmend.commons.RestautantData;

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
        String sql = "INSERT INTO RESTAURANTS (zipcode, address, roadAddress, zonecode, storeName, type, xpos, ypos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
                            break root;
                        }
                    } catch (Exception e) {
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