package com.unknown.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

	public static <T> T readFile(String fileName, T context,
						LineFilter<T> filter, LineIterator<T> iter) throws IOException{
		BufferedReader reader = null;
		boolean isFilter = false;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"gb2312"));
			String content;
			do{
				content = reader.readLine();
				if(content != null){					
					if(filter != null){
						isFilter = filter.filter(context, content);
					}
					if(!isFilter && iter != null){
						iter.readLine(context, content);
					}
				}
			}while(content != null);
		}finally{
			if(reader != null){
				reader.close();
			}
		}
		return context;
	}
}
