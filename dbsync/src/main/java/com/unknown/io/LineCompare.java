package com.unknown.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LineCompare {

	public static void main(String[] args) throws IOException {
		
		Map<String,String> crawler = new HashMap<String, String>();
		
		Map<String,String> dataSource = new HashMap<String, String>();

		FileUtils.readFile("d:\\keyword\\crawler.txt", crawler, null, new LineIterator<Map<String,String>>() {
			@Override
			public void readLine(Map<String, String> context, String line) {
				context.put(line, "0");				
			}			
		});
		
		FileUtils.readFile("d:\\keyword\\datasource.txt", dataSource, null, new LineIterator<Map<String,String>>() {
			@Override
			public void readLine(Map<String, String> context, String line) {
				context.put(line, "0");
			}			
		});
		
		for(String key : crawler.keySet()){
			dataSource.remove(key);
		}
		
		for(String key : dataSource.keySet()){
			System.out.println(key);
		}
		
		System.out.println("----------------------");
	}

}
