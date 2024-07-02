package BrellaFormValidation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static List<String[]> readExcel(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            String[] rowData = new String[row.getLastCellNum()];
            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                rowData[cn] = row.getCell(cn).toString();
            }
            data.add(rowData);
        }
        workbook.close();
        fis.close();
        return data;
    }
}
