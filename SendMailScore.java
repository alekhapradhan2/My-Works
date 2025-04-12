package ApiTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class SendMailScore {
	
	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("webdriver", "chromedriver.exe");
		WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);	 
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.cricbuzz.com/live-cricket-scores/115129/pbks-vs-srh-27th-match-indian-premier-league-2025");
		
		String score1=driver.findElement(By.xpath("//div[@ng-show='isMiniscoreRendered']//div[contains(@class,'cb-col-scores')]//div[@class='cb-min-bat-rw']//h2")).getText();
		String insideParentheses1= score1.replaceAll(".*\\((.*?)\\).*", "$1").split("\\.")[0];
		
		Integer over=Integer.parseInt(insideParentheses1);
		over++;
		
		for(int i=1;i<=21000000;i++)
		{
//			driver.navigate().refresh();
//			String score2=driver.findElement(By.xpath("//div[@ng-show='isMiniscoreRendered']//div[contains(@class,'cb-col-scores')]//div[@class='cb-min-bat-rw']//h2")).getText();
			score1=driver.findElement(By.xpath("//div[@ng-show='isMiniscoreRendered']//div[contains(@class,'cb-col-scores')]//div[@class='cb-min-bat-rw']//h2")).getText();
			insideParentheses1= score1.replaceAll(".*\\((.*?)\\).*", "$1").split("\\.")[0];
			if(insideParentheses1.equals(""+over+""))
			{
				score1=driver.findElement(By.xpath("//div[@ng-show='isMiniscoreRendered']//div[contains(@class,'cb-col-scores')]//div[@class='cb-min-bat-rw']//h2")).getText();
				String score3=driver.findElement(By.xpath("//div[@ng-show='isMiniscoreRendered']//div[contains(@class,'cb-col-scores')]//div[@class='cb-min-bat-rw']")).getText();
				String totalSc=score3;
			       // -------- BATTING DATA EXTRACTION --------
		       
		        List<WebElement> battingRows = driver.findElements(By.xpath("//div[@ng-repeat='batsmen in match.miniscore.batsman']"));
		        List<String >bat=new ArrayList<String>();
		        List<String >ball=new ArrayList<String>();
		        
		        
		        for (WebElement row : battingRows) {
		            List<WebElement> cols = row.findElements(By.cssSelector("div"));
		            if (cols.size() >= 6) {
		                String name = cols.get(0).getText();
		                String runs = cols.get(1).getText();
		                String balls = cols.get(2).getText();
		                String fours = cols.get(3).getText();
		                String sixes = cols.get(4).getText();
		                String strikeRate = cols.get(5).getText();

		               String bt=name + " - R: " + runs + ", B: " + balls + ", 4s: " + fours + ", 6s: " + sixes + ", SR: " + strikeRate;

		               bat.add(bt);
		               
		                
		            }
		        }

		        // -------- BOWLING DATA EXTRACTION --------
		      
		        List<WebElement> bowlingRows = driver.findElements(By.xpath("//div[@ng-repeat='bowler in match.miniscore.bowler']"));

		        for (WebElement row : bowlingRows) {
		            List<WebElement> cols = row.findElements(By.cssSelector("div"));
		            if (cols.size() >= 6) {
		                String name = cols.get(0).getText();
		                String overs = cols.get(1).getText();
		                String maidens = cols.get(2).getText();
		                String runs = cols.get(3).getText();
		                String wickets = cols.get(4).getText();
		                String economy = cols.get(5).getText();

		                 String bow=name + " - Over: " + overs + ", M: " + maidens + ", R: " + runs + ", W: " + wickets + ", ECO: " + economy;

			                	ball.add(bow);
			
		            }
		        }
		        String bt="";
		        for(String a:bat)
		        {
		        	bt=bt+ "\n" +a;
		        }
		        
		        String bow="";
		        for(String a:ball)
		        {
		        	bow=bow+ "\n" +a;
		        }
		        System.out.println(totalSc+ "\n" +"\n------ BATTING ------"+ "\n"+bt+"\n------ BOWLING ------"+ "\n" +bow);
		        SendMail("\n------ BATTING ------"+ "\n"+bt+"\n------ BOWLING ------"+ "\n" +bow, totalSc);
		        over++;
		        }
				
			}
			Thread.sleep(5000);
		}

	
		

		

	
	public static void SendMail(String content,String sub)
	{
	    final String username = "alekhpradhan18@gmail.com";     
	    final String password = "fzqp anhv bwht xzyc";          

	    Properties props = new Properties();
	  
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });

	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("alekhpradhan18@gmail.com"));
	        String id[]= {"manupradhan7887@gmail.com","alekhpradhan18@gmail.com"};
	        for(String mailId:id)
	        {
		        message.setRecipients(
		                Message.RecipientType.TO,
		                InternetAddress.parse(mailId)
		        );
	        }

	        message.setSubject(sub);
	        message.setText(content);

	        Transport.send(message);

	        System.out.println("Email sent successfully!");

	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
	}
	


}
