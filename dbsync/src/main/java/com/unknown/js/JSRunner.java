package com.unknown.js;

import java.io.IOException;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.unknown.io.FileUtils;
import com.unknown.io.LineIterator;

public class JSRunner {

	public static void main(String[] args) throws IOException, InterruptedException {
		String[] context = {""};
		FileUtils.readFile("D:\\workspace\\ws2\\yuankit\\11.js", context, null, new LineIterator<String[]>() {
			@Override
			public void readLine(String[] context, String line)throws IOException {
				context[0] = context[0] + line;
			}			
		});
		
		System.out.println(runJS(context[0]));
	}

	public static String runJS(String js) throws InterruptedException, FailingHttpStatusCodeException, IOException{
//		WebClient ieXplorer = new WebClient(BrowserVersion.FIREFOX_24);
//		ieXplorer.set
//		HtmlPage page = ieXplorer.getPage("file://D:\\workspace\\ws2\\yuankit\\11.html");
//		page.executeJavaScript("ff()");
//		InternetExplorerDriver driver = new InternetExplorerDriver();
//		driver.get(new URL("file://D:\\workspace\\ws2\\yuankit\\11.html").toString());
//		driver.get("http://index.baidu.com/static/js/raphael.js");
//		return (String)driver.executeScript("ff()");
//		Thread.sleep(10000);
//		return (String)driver.executeScript("ff()");
		
		HtmlUnitDriver htmlDriver = new HtmlUnitDriver();
		htmlDriver.setJavascriptEnabled(true);
		htmlDriver.get("file://D:\\workspace\\ws2\\yuankit\\11.html");
		//htmlDriver.executeAsyncScript("ff()");
		return "";
	}
}
