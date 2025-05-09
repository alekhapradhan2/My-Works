import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class TokenExporter {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		String  farmerDetail="",tokenfile="",ppcCode = "";
		String userName="",password="",pacdID="";
		Scanner sc=new Scanner(System.in);
		System.out.println("Please give the Society Id");
		Thread.sleep(2000);
		System.out.print("For Badapalanka Press B and for Gopinathpur press G ");
		ppcCode=sc.next();
		
		switch (ppcCode.toUpperCase()) {
		case "G":
			userName="S1110717";
			password="Kunjit@2024";
			farmerDetail="src/Datas/Gopinathpur/Farmer_Details.xlsx";
			tokenfile="src/Datas/Gopinathpur/Token_Details.xlsx";
			break;
		case "B":
			userName="S1110703";
			password="BIPRA@321";
			farmerDetail="src/Datas/Badapalanka/Farmer_Details.xlsx";
			tokenfile="src/Datas/Badapalanka/Token_Details.xlsx";
			break;

		default:
			break;
		}
		
		System.setProperty("webdriver", "chromedriver.exe");
		WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);	 
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://ppas.pdsodisha.gov.in");
		driver.findElement(By.id("txtuserID")).sendKeys(userName);
		driver.findElement(By.id("txtPassword")).sendKeys(password);
		Thread.sleep(7000);
		driver.findElement(By.id("LoginButton")).click();
		driver.findElement(By.className("icon-menu")).click();
		driver.findElement(By.xpath("//span[text()='Purchase']")).click();
		driver.findElement(By.linkText("Mandi Arrival")).click();
		Select ppcid=new Select(driver.findElement(By.id("ppcId")));
		ppcid.selectByIndex(1);
        FileInputStream fis = new FileInputStream(new File(farmerDetail));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet=workbook.getSheet("Sheet1");
        FileInputStream fis1 = new FileInputStream(new File(tokenfile));
        Workbook workbook1 = new XSSFWorkbook(fis1);
        Sheet sheet1=workbook1.getSheet("Sheet1");
        int rowcoutn=1;
        double tokenTokenQty=0;
        double tokenRemainQty=0;
        int totalFarmrget=0;
        int totalFarmerNot=0;
        int totalToken=0;
		for(int i=1;i<=300;i++)
		{
			try {
				Row row=sheet.getRow(i);
				org.apache.poi.ss.usermodel.Cell cell=row.getCell(2);
				org.apache.poi.ss.usermodel.Cell cell1=row.getCell(1);
				
				String farmerCode=cell.getStringCellValue();
				String farmerName=cell1.getStringCellValue();
				driver.findElement(By.id("farmerCodeId")).sendKeys(farmerCode);
				driver.findElement(By.id("searchTokenViewBtnId")).click();
		        
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				List<WebElement> nodata = driver.findElements(By.className("text-danger"));
				List<WebElement> rows=driver.findElements(By.xpath("//table[@id='viewPacsTableId']//tbody//tr"));
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // Reset after
				if(!rows.isEmpty())
				{
					totalFarmrget++;

					for(int j=0;j<rows.size();j++)
					{
						totalToken++;
						List<WebElement>col=rows.get(j).findElements(By.tagName("td"));
						String farmercode=col.get(4).getText().trim();
						String farmername=col.get(5).getText().trim();
						String tokenNo=col.get(6).getText().trim();
						String tokenDate=col.get(7).getText().trim();
						String tokenQty=col.get(8).getText().trim();
					    double tokenQtyValue = Double.parseDouble(tokenQty);
					    tokenTokenQty=tokenTokenQty+tokenQtyValue;
						String remaintokenQty=col.get(9).getText().trim();
						double RemtokenQtyValue = Double.parseDouble(remaintokenQty);
						tokenRemainQty=tokenRemainQty+RemtokenQtyValue;
						Row row1=sheet1.getRow(rowcoutn);
						Cell fc=row1.getCell(0);
						fc.setCellValue(farmercode);
						Cell fn=row1.getCell(1);
						fn.setCellValue(farmername);
						if(tokenQtyValue!=RemtokenQtyValue)
						{
							Cell tn=row1.getCell(2);
							tn.setCellValue(tokenNo+" Purchased-qty "+(tokenQtyValue-RemtokenQtyValue));
						}else {
							Cell tn=row1.getCell(2);
							tn.setCellValue(tokenNo);
						}

						Cell td=row1.getCell(3);
						td.setCellValue(tokenDate);
						Cell tq=row1.getCell(4);
						tq.setCellValue(tokenQtyValue);
						Cell rtq=row1.getCell(5);
						rtq.setCellValue(RemtokenQtyValue);
						rowcoutn++;
						FileOutputStream fos = new FileOutputStream(new File(tokenfile));
						workbook1.write(fos);
						System.out.println("|------------------------------------------------------------------|");
						System.out.println("|"+farmerName+"  "+farmerCode+"  "+tokenQtyValue+"     " );
						System.out.println("|Updated Successfully                                              |");
						
					}
				}else {
					totalFarmerNot++;
					Row row1=sheet1.getRow(rowcoutn);
					Cell fc=row1.getCell(0);
					fc.setCellValue(farmerCode);
					Cell fn=row1.getCell(1);
					fn.setCellValue(farmerName);
					Cell tn=row1.getCell(2);
					tn.setCellValue("Token Not Generated");
					FileOutputStream fos = new FileOutputStream(new File(tokenfile));
					workbook1.write(fos);
					System.out.println("|------------------------------------------------------------------|");
					System.out.println("|"+farmerName+"  "+farmerCode+"                   " );
					System.out.println("|Token Not Generated                                               |");
					rowcoutn++;
				}
				driver.findElement(By.id("farmerCodeId")).clear();
			} catch (Exception e) {
				
			}

			
		}
		System.out.println("|------------------------------------------------------------------|");
		System.out.println("Total Token Qty: "+tokenTokenQty);
		System.out.println("Total Remaining Token Qty: "+tokenRemainQty);
		System.out.println("Total Farmer Got the toekn: "+totalFarmrget);
		System.out.println("Total Farmer Did not Get the toekn: "+totalFarmerNot);
		System.out.println("Total token Generated: "+totalToken);
		Row row1=sheet1.getRow(rowcoutn);
		Cell total=row1.getCell(3);
		total.setCellValue("Total");
		Cell fc=row1.getCell(4);
		fc.setCellValue(tokenTokenQty);
		Cell fn=row1.getCell(5);
		fn.setCellValue(tokenRemainQty);
		rowcoutn++;
		row1=sheet1.getRow(rowcoutn);
		Cell tt=row1.getCell(2);
		tt.setCellValue("Total Active Token: "+totalToken);
		FileOutputStream fos = new FileOutputStream(new File(tokenfile));
		workbook1.write(fos);
		driver.close();
		System.out.println("|--------------------Token Data Exported----------------------------|");
		
		

		
	}

}
