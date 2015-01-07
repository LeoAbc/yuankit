package com.unknown.io;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Encode {

	public static void main(String[] args) throws IOException{
		Set<Character> dic = new HashSet<Character>();
		FileUtils.readFile("D:\\Users\\thluo\\Downloads\\´óÇØµÛ¹ú.txt", dic , null, new LineIterator<Set<Character>>() {
			@Override
			public void readLine(Set<Character> context, String line)throws IOException {
				for(char c : line.toCharArray()){
					context.add(c);
				}
			}			
		});
		
		int newLine = 100;
		for(Character c : dic){
			System.out.print("'" + c + "',");
			if(--newLine == 0){
				newLine = 100;
				System.out.println();
			}
		}		
	}
}
