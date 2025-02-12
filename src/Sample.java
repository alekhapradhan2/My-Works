import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sample {
	
	public static void main(String[] args) throws IOException {
		String tokenfile="C:\\Users\\Dell\\Documents\\Token Details.xlsx";
        FileInputStream fis1 = new FileInputStream(new File(tokenfile));
        Workbook workbook1 = new XSSFWorkbook(fis1);
        Sheet sheet1=workbook1.getSheet("Sheet1");
        int rowsize=sheet1.getLastRowNum();
        double totalQty=0;
        for(int i=1;i<=rowsize;i++)
        {
        	Row row=sheet1.getRow(i);
        	Cell cell1=row.getCell(4);
        	double tq=cell1.getNumericCellValue();
        	
        	totalQty=totalQty+tq;
        	
        	
        }
        System.out.println(totalQty);
		 
	}

}
