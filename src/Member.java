import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Member {

	public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);	 
   	   driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
   	   driver.manage().window().maximize();
   	   driver.get("https://ghtn.affnetz.com/login");
   	   driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
   	   driver.findElement(By.id("txtEmail")).sendKeys("engineering+ghtn@affnetz.com");
   	   driver.findElement(By.id("txtPassword")).sendKeys("A@pr0d*2gHtN");
   	   driver.findElement(By.xpath("//button[@type='submit']")).click();
   	   driver.findElement(By.xpath("//a[contains(@href,'embers')]")).click();
		String memberFile="C:\\Users\\Dell\\Documents\\member.xlsx";
        FileInputStream fis = new FileInputStream(new File(memberFile));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet=workbook.getSheet("Sheet1");
        int rowcount=sheet.getPhysicalNumberOfRows();
        for(int i=1;i<rowcount;i++)
        {
        	Row row=sheet.getRow(i);
        	Cell fname=row.getCell(0);
        	String firstName=fname.getStringCellValue();
        	Cell lname=row.getCell(1);
        	String lastName=lname.getStringCellValue();
        	Cell mail=row.getCell(3);
        	String mailId=mail.getStringCellValue();
        	driver.findElement(By.xpath("//input[@placeholder='Name & Email']")).sendKeys(mailId);
        	Thread.sleep(3000);
        	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        	List<WebElement> card=driver.findElements(By.xpath("//div[@class='row mt-4 align-end']//div[@class='mt-2 col-sm-6 col-md-4 col']"));
        	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        	if(!card.isEmpty())
        	{
        		String cardname=driver.findElement(By.xpath("//div[@class='v-card__title']//span")).getText();
        		String cardMail=driver.findElement(By.xpath("(//div[@class='v-list-item__content pa-1']//div[@class='v-list-item__title'])[1]")).getText();
        		String usertype=driver.findElement(By.xpath("(//div[@class='v-list-item__content pa-1']//div[@class='v-list-item__title'])[3]")).getText();
        		if(cardname.equals(firstName+" "+lastName))
        		{
        			Cell cell1=row.getCell(8);
        			cell1.setCellValue("Name Matched");
        		}else {
        			Cell cell1=row.getCell(8);
        			cell1.setCellValue("Name Missmatched  "+cardname );
        		}
        		if(usertype.equals("Volunteers"))
        		{
        			Cell cell1=row.getCell(9);
        			cell1.setCellValue("UserType Matched");
        		}else {
        			Cell cell1=row.getCell(9);
        			cell1.setCellValue(usertype);
        		}
        		
        		Cell cell2=row.getCell(7);
        		cell2.setCellValue("User exist");
				FileOutputStream fos = new FileOutputStream(new File(memberFile));
				workbook.write(fos);
				System.out.println(i+" "+mailId+" "+"Exist");
        	}else {
        		Cell cell2=row.getCell(7);
        		cell2.setCellValue("User Not found");
				FileOutputStream fos = new FileOutputStream(new File(memberFile));
				workbook.write(fos);
				System.out.println(i+" "+mailId+" "+"Not Exist");
        	}
        	
        	driver.findElement(By.xpath("//button[@aria-label='clear icon']")).click();
        	
        	
        }

	}

}
